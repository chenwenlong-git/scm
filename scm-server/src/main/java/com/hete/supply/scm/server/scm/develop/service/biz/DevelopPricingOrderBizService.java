package com.hete.supply.scm.server.scm.develop.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopPricingOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopPricingOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.develop.converter.DevelopPricingOrderConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopPricingOrderInfoConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopPricingOrderMechanismConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopSampleOrderConverter;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopOrderPriceCreateBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.*;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.handler.DevelopStatusHandler;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopPricingOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/2 18:31
 */
@Service
@RequiredArgsConstructor
public class DevelopPricingOrderBizService {

    private final DevelopPricingOrderBaseService developPricingOrderBaseService;
    private final DevelopPricingOrderDao developPricingOrderDao;
    private final SupplierDao supplierDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopPricingOrderMechanismDao developPricingOrderMechanismDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopChildBaseService developChildBaseService;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final DevelopChildOrderChangeDao developChildOrderChangeDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final DevelopChildOrderAttrDao developChildOrderAttrDao;
    private final DevelopOrderPriceDao developOrderPriceDao;

    /**
     * 查询核价单列表
     *
     * @param dto:
     * @return PageInfo<DevelopPricingOrderSearchVo>
     * @author ChenWenLong
     * @date 2023/8/3 11:14
     */
    public CommonPageResult.PageInfo<DevelopPricingOrderSearchVo> search(DevelopPricingOrderSearchDto dto) {
        //条件过滤
        if (null == developPricingOrderBaseService.getSearchWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<DevelopPricingOrderSearchVo> pageResult = developPricingOrderDao.search(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<DevelopPricingOrderSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }
        List<String> developPricingOrderNoList = records.stream().map(DevelopPricingOrderSearchVo::getDevelopPricingOrderNo).distinct().collect(Collectors.toList());
        List<String> developChildOrderNoList = records.stream().map(DevelopPricingOrderSearchVo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        Map<String, List<DevelopPricingOrderInfoPo>> developPricingOrderInfoPoMap = developPricingOrderInfoDao.getListByDevelopPricingOrderNoList(developPricingOrderNoList).stream()
                .collect(Collectors.groupingBy(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo));

        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderPoList
                .stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));

        // 获取图片
        Map<String, List<String>> developChildOrderStyleImg = developChildBaseService.getDevelopChildOrderStyleImg(developChildOrderPoList);

        // 供应商
        Map<String, SupplierPo> supplierPoMap = supplierDao.getSupplierMapBySupplierCodeList(records.stream().map(DevelopPricingOrderSearchVo::getSupplierCode).distinct().collect(Collectors.toList()));
        List<DevelopSampleOrderPo> sampleOrderPoList = developSampleOrderDao.getListByDevelopPricingOrderNoList(developPricingOrderNoList);
        List<String> developSampleOrderNoList = sampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoMap = sampleOrderPoList.stream()
                .collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopPricingOrderNo));

        // 查询渠道大货价格
        List<String> newDevelopSampleOrderNoList = new ArrayList<>(developSampleOrderNoList);
        newDevelopSampleOrderNoList.addAll(developPricingOrderNoList);
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(newDevelopSampleOrderNoList,
                List.of(DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE, DevelopOrderPriceType.PRICING_PURCHASE_PRICE));

        for (DevelopPricingOrderSearchVo record : records) {
            List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList = developPricingOrderInfoPoMap.get(record.getDevelopPricingOrderNo());
            record.setPricingDevelopSampleOrderSearchVoList(DevelopPricingOrderConverter.poListToSearchVoList(developPricingOrderInfoPoList, developOrderPricePoList));

            if (StringUtils.isNotBlank(record.getSupplierCode())) {
                record.setSupplierType(supplierPoMap.get(record.getSupplierCode()).getSupplierType());
            }
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderPoMap.get(record.getDevelopPricingOrderNo());
            if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
                record.setPricingDevelopSampleOrderList(DevelopSampleOrderConverter.INSTANCE.convert(developSampleOrderPoList));
            }
            DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(record.getDevelopChildOrderNo());
            if (developChildOrderPo != null) {
                record.setIsSample(developChildOrderPo.getIsSample());
            }
            record.setFileCodeList(developChildOrderStyleImg.get(record.getDevelopChildOrderNo()));
        }

        return pageResult;

    }

    /**
     * 查询核价单详情
     *
     * @param dto:
     * @return DevelopPricingOrderDetailVo
     * @author ChenWenLong
     * @date 2023/8/3 11:14
     */
    public DevelopPricingOrderDetailVo detail(DevelopPricingOrderDetailDto dto) {
        DevelopPricingOrderPo developPricingOrderPo = developPricingOrderDao.getByDevelopPricingOrderNo(dto.getDevelopPricingOrderNo());
        if (developPricingOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPricingOrderPo.getDevelopChildOrderNo());
        if (developChildOrderPo == null) {
            throw new BizException("开发子单的数据查询不到，请刷新页面后重试！");
        }

        //关联样品单
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
        List<String> developSampleOrderNoList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());
        Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderPoList.stream().collect(Collectors.toMap(DevelopSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));

        List<String> developSampleSkuList = developSampleOrderPoList.stream()
                .filter(w -> w.getSku() != null && !w.getSku().isEmpty())
                .map(DevelopSampleOrderPo::getSku)
                .distinct().collect(Collectors.toList());

        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(developSampleSkuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(developSampleSkuList);
        }

        //供应商
        List<String> supplierCodeList = new ArrayList<>(List.of(developPricingOrderPo.getSupplierCode()));
        supplierCodeList.addAll(developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getSupplierCode).distinct().collect(Collectors.toList()));
        Map<String, SupplierType> supplierTypeMap = supplierDao.getBySupplierCodeList(supplierCodeList).stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, SupplierPo::getSupplierType));

        //样品信息
        List<DevelopPricingOrderInfoPo> infoPoList = developPricingOrderInfoDao.getListByDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
        List<DevelopPricingOrderMechanismPo> mechanismPoList = developPricingOrderMechanismDao.getListByDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());

        Map<String, DevelopPricingOrderInfoPo> infoMap = infoPoList.stream()
                .collect(Collectors.toMap(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo, Function.identity()));
        Map<String, List<DevelopPricingOrderMechanismPo>> mechanismMap = mechanismPoList.stream()
                .collect(Collectors.groupingBy(DevelopPricingOrderMechanismPo::getDevelopSampleOrderNo));

        //关联审版单样品单生产属性
        List<String> developSampleOrderNoPricingList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoPricingList);
        Map<String, List<DevelopReviewSampleOrderInfoPo>> developReviewSampleOrderInfoPoMap = developReviewSampleOrderInfoPoList.stream()
                .collect(Collectors.groupingBy(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo));

        //查询开发子单生产属性
        List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());

        // 查询渠道大货价格
        List<String> newDevelopSampleOrderNoList = new ArrayList<>(developSampleOrderNoList);
        newDevelopSampleOrderNoList.add(developPricingOrderPo.getDevelopPricingOrderNo());
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(newDevelopSampleOrderNoList,
                List.of(DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE, DevelopOrderPriceType.PRICING_PURCHASE_PRICE));

        //无需打样处理逻辑
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            //无需打样获取关联详情信息
            Map<String, List<DevelopPricingOrderInfoPo>> infoPricingMap = infoPoList.stream()
                    .collect(Collectors.groupingBy(DevelopPricingOrderInfoPo::getDevelopPricingOrderNo));
            Map<String, List<DevelopPricingOrderMechanismPo>> mechanismPricingMap = mechanismPoList.stream()
                    .collect(Collectors.groupingBy(DevelopPricingOrderMechanismPo::getDevelopPricingOrderNo));

            return DevelopPricingOrderConverter.developPricingOrderPoToIsSampleVo(developPricingOrderPo, infoPricingMap, mechanismPricingMap
                    , supplierTypeMap, developChildOrderPo, developChildOrderAttrPoList, developOrderPricePoList);

        } else {
            return DevelopPricingOrderConverter.developPricingOrderPoToVo(developPricingOrderPo, infoMap, mechanismMap,
                    developSampleOrderPoMap, supplierTypeMap, skuEncodeMap, developChildOrderPo, developChildOrderAttrPoList,
                    developReviewSampleOrderInfoPoMap, developOrderPricePoList);
        }

    }

    /**
     * 核价单列表导出
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2023/8/3 11:14
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopPricingOrderSearchDto dto) {
        //条件过滤
        if (null == developPricingOrderBaseService.getSearchWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developPricingOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_DEVELOP_PRICING_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 核价页面确认核价
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2023/8/3 11:14
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitPricing(DevelopPricingOrderSubmitPricingDto dto) {
        DevelopPricingOrderPo developPricingOrderPo = developPricingOrderDao.getByDevelopPricingOrderNo(dto.getDevelopPricingOrderNo());
        if (developPricingOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        DevelopPricingOrderStatus toAlreadyPrice = developPricingOrderPo.getDevelopPricingOrderStatus().toAlreadyPrice();

        SupplierPo supplierPo = supplierDao.getBySupplierCode(developPricingOrderPo.getSupplierCode());
        if (supplierPo == null) {
            throw new BizException("供应商数据已被修改或删除，请刷新页面后重试！");
        }

        DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPricingOrderPo.getDevelopChildOrderNo());
        if (developChildOrderPo == null) {
            throw new BizException("开发子单的数据查询不到，请刷新页面后重试！");
        }
        DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        if (developPamphletOrderPo == null) {
            throw new BizException("版单的数据查询不到，请刷新页面后重试！");
        }
        final DevelopChildOrderChangePo developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(developPricingOrderPo.getDevelopChildOrderNo());
        if (developChildOrderChangePo == null) {
            throw new BizException("开发子单的数据查询不到，请刷新页面后重试！");
        }

        //更新开发子单状态
        developChildOrderPo.setDevelopChildOrderStatus(developChildOrderPo.getDevelopChildOrderStatus().toNewest());
        developChildOrderDao.updateByIdVersion(developChildOrderPo);

        //开发子单状态推送PLM
        final DevelopStatusDto developStatusDto = new DevelopStatusDto();
        developStatusDto.setDevelopChildOrderStatus(developChildOrderPo.getDevelopChildOrderStatus());
        developStatusDto.setKey(developChildOrderPo.getDevelopChildOrderNo());
        developStatusDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
        developStatusDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        developStatusDto.setSupplierCode(developChildOrderPo.getSupplierCode());
        developStatusDto.setSupplierName(developChildOrderPo.getSupplierName());
        developStatusDto.setUserKey(GlobalContext.getUserKey());
        developStatusDto.setUsername(GlobalContext.getUsername());
        developStatusDto.setStatusUpdateTime(LocalDateTime.now());
        consistencySendMqService.execSendMq(DevelopStatusHandler.class, developStatusDto);

        List<DevelopPricingOrderInfoPo> insertInfoPoList = new ArrayList<>();
        List<DevelopPricingOrderMechanismPo> insertMechanismPoList = new ArrayList<>();
        // 渠道大货价格
        List<DevelopOrderPriceCreateBo> developOrderPriceCreateBoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(dto.getDevelopPricingOrderInfoList())) {
            for (DevelopPricingOrderInfoDto developPricingOrderInfoDto : dto.getDevelopPricingOrderInfoList()) {
                DevelopPricingOrderInfoPo developPricingOrderInfoPo = DevelopPricingOrderInfoConverter.INSTANCE.convert(developPricingOrderInfoDto);
                developPricingOrderInfoPo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
                developPricingOrderInfoPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                developPricingOrderInfoPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                developPricingOrderInfoPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                insertInfoPoList.add(developPricingOrderInfoPo);

                if (CollectionUtils.isNotEmpty(developPricingOrderInfoDto.getDevelopPricingOrderMechanismList())) {
                    List<DevelopPricingOrderMechanismPo> developPricingOrderMechanismPoList = DevelopPricingOrderMechanismConverter.dtoToPoList(developPricingOrderPo,
                            developPricingOrderInfoDto.getDevelopSampleOrderNo(),
                            developPricingOrderInfoDto.getDevelopPricingOrderMechanismList());
                    insertMechanismPoList.addAll(developPricingOrderMechanismPoList);
                }

                // 更新供渠道应商报价大货价格
                // 无需打样时关联核价单号&类型为PRICING_NOT_PURCHASE_PRICE
                DevelopOrderPriceCreateBo developOrderPriceCreateBo = new DevelopOrderPriceCreateBo();
                if (StringUtils.isBlank(developPricingOrderInfoDto.getDevelopSampleOrderNo())) {
                    developOrderPriceCreateBo.setDevelopOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
                    developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE);
                }
                // 需打样时关联样品单号&类型为PRICING_PURCHASE_PRICE
                if (StringUtils.isNotBlank(developPricingOrderInfoDto.getDevelopSampleOrderNo())) {
                    developOrderPriceCreateBo.setDevelopOrderNo(developPricingOrderInfoDto.getDevelopSampleOrderNo());
                    developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.PRICING_PURCHASE_PRICE);
                }
                List<DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList = Optional.ofNullable(developPricingOrderInfoDto.getDevelopOrderPriceList()).orElse(new ArrayList<>()).stream().map(item -> {
                    DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo developOrderPriceCreateItemBo = new DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo();
                    developOrderPriceCreateItemBo.setChannelId(item.getChannelId());
                    developOrderPriceCreateItemBo.setPrice(item.getPrice());
                    return developOrderPriceCreateItemBo;
                }).collect(Collectors.toList());
                developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
                developOrderPriceCreateBoList.add(developOrderPriceCreateBo);

            }
        }

        if (CollectionUtils.isNotEmpty(insertInfoPoList)) {
            developPricingOrderInfoDao.insertBatch(insertInfoPoList);
        }

        if (CollectionUtils.isNotEmpty(insertMechanismPoList)) {
            developPricingOrderMechanismDao.insertBatch(insertMechanismPoList);
        }

        developChildBaseService.developOrderPriceBatchSave(developOrderPriceCreateBoList);


        //更新核价单
        developPricingOrderPo.setDevelopPricingOrderStatus(toAlreadyPrice);
        developPricingOrderPo.setNuclearPriceUser(GlobalContext.getUserKey());
        developPricingOrderPo.setNuclearPriceUsername(GlobalContext.getUsername());
        developPricingOrderPo.setNuclearPriceTime(LocalDateTime.now());

        developPricingOrderDao.updateByIdVersion(developPricingOrderPo);

        // 更新开发子单的上新时间
        developChildOrderChangePo.setNuclearPriceUser(GlobalContext.getUserKey());
        developChildOrderChangePo.setNuclearPriceUsername(GlobalContext.getUsername());
        developChildOrderChangePo.setPricingCompletionDate(LocalDateTime.now());
        developChildOrderChangeDao.updateByIdVersion(developChildOrderChangePo);

        // 开发子单日志
        developChildBaseService.createStatusChangeLog(developChildOrderPo, "核价单" + developPricingOrderPo.getDevelopPricingOrderNo() + "完成，进入已核价状态");
    }

    /**
     * 提交核价
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2023/8/3 11:46
     */
    @Transactional(rollbackFor = Exception.class)
    public void submit(DevelopPricingOrderSubmitDto dto) {
        List<String> developPricingOrderNoList = dto.getDevelopPricingOrderDtoList().stream()
                .map(DevelopPricingOrderSubmitDto.DevelopPricingOrderDtoList::getDevelopPricingOrderNo)
                .collect(Collectors.toList());

        List<String> developSampleOrderNoList = dto.getDevelopPricingOrderDtoList().stream()
                .filter(dtoList -> dtoList.getDevelopSampleOrderNoList() != null && !dtoList.getDevelopSampleOrderNoList().isEmpty())
                .flatMap(dtoList -> dtoList.getDevelopSampleOrderNoList().stream())
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(developSampleOrderNoList)) {
            if (developSampleOrderNoList.size() != developSampleOrderNoList.stream().distinct().count()) {
                throw new ParamIllegalException("禁止选择重复的样品单，请重新选择！");
            }
        }

        List<DevelopPricingOrderPo> developPricingOrderPoList = developPricingOrderDao.getByDevelopPricingOrderNoList(developPricingOrderNoList);
        if (CollectionUtils.isEmpty(developPricingOrderPoList)) {
            throw new BizException("数据异常，请联系管理员后重试！");
        }
        Map<String, DevelopPricingOrderPo> developPricingOrderPoMap = developPricingOrderPoList.stream().collect(Collectors
                .toMap(DevelopPricingOrderPo::getDevelopPricingOrderNo, developPricingOrderPo -> developPricingOrderPo));

        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);

        //查询审版单样品信息
        List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);

        //更新的核价单
        List<DevelopPricingOrderPo> updateDevelopPricingOrderList = new ArrayList<>();

        for (DevelopPricingOrderSubmitDto.DevelopPricingOrderDtoList developPricingOrderDtoList : dto.getDevelopPricingOrderDtoList()) {
            DevelopPricingOrderPo developPricingOrderPo = developPricingOrderPoMap.get(developPricingOrderDtoList.getDevelopPricingOrderNo());
            if (developPricingOrderPo == null) {
                throw new BizException("查询不到核价单相关数据，请刷新页面后重试！");
            }
            developPricingOrderPo.setDevelopPricingOrderStatus(developPricingOrderPo.getDevelopPricingOrderStatus().toWaitPrice());
            developPricingOrderPo.setSubmitUser(GlobalContext.getUserKey());
            developPricingOrderPo.setSubmitUsername(GlobalContext.getUsername());
            developPricingOrderPo.setNuclearPriceUser(developPricingOrderDtoList.getNuclearPriceUser());
            developPricingOrderPo.setNuclearPriceUsername(developPricingOrderDtoList.getNuclearPriceUsername());
            developPricingOrderPo.setSubmitTime(LocalDateTime.now());
            updateDevelopPricingOrderList.add(developPricingOrderPo);
        }

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            for (DevelopPricingOrderSubmitDto.DevelopPricingOrderDtoList developPricingOrderDtoList : dto.getDevelopPricingOrderDtoList()) {
                if (developPricingOrderDtoList.getDevelopSampleOrderNoList().contains(developSampleOrderPo.getDevelopSampleOrderNo())) {
                    developSampleOrderPo.setDevelopPricingOrderNo(developPricingOrderDtoList.getDevelopPricingOrderNo());
                    developSampleOrderPo.setDevelopSampleStatus(developSampleOrderPo.getDevelopSampleStatus().toAlreadyGetPricing());

                    //获取样品处理方式
                    developReviewSampleOrderPoList.stream()
                            .filter(w -> w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                            .findFirst()
                            .ifPresent(developReviewSampleOrderPo -> developSampleOrderPo.setDevelopSampleMethod(developReviewSampleOrderPo.getDevelopSampleMethod()));

                }
            }
        }

        developPricingOrderDao.updateBatchByIdVersion(updateDevelopPricingOrderList);

        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        // 开发子单日志
        List<String> developChildOrderNoList = developPricingOrderPoList.stream()
                .map(DevelopPricingOrderPo::getDevelopChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            developPricingOrderPoList.stream()
                    .filter(pricingOrderPo -> developChildOrderPo.getDevelopChildOrderNo().equals(pricingOrderPo.getDevelopChildOrderNo()))
                    .findFirst()
                    .ifPresent(developPricingOrderPo -> developChildBaseService.createStatusChangeLog(developChildOrderPo, "提交核价单" + developPricingOrderPo.getDevelopPricingOrderNo()));
        }

    }
}
