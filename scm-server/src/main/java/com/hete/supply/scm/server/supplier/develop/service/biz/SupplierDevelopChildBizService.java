package com.hete.supply.scm.server.supplier.develop.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopChildNoDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopOrderPriceSaveDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPamphletOrderRawDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.supplier.develop.converter.SupplierDevelopConverter;
import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletCompleteDto;
import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletExamineDto;
import com.hete.supply.scm.server.supplier.develop.entity.dto.DevelopPamphletOrderRawItemDto;
import com.hete.supply.scm.server.supplier.develop.enums.DevelopPamphletExamine;
import com.hete.supply.scm.server.supplier.entity.dto.PamphletReturnRawDto;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.scm.server.supplier.settle.service.biz.SupplierDeliverBizService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/3 14:17
 */
@Service
@RequiredArgsConstructor
@Validated
public class SupplierDevelopChildBizService {

    private final DevelopPamphletOrderRawDao developPamphletOrderRawDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final AuthBaseService authBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopChildBaseService developChildBaseService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopReviewOrderDao developReviewOrderDao;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final DevelopChildOrderAttrDao developChildOrderAttrDao;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final DevelopChildOrderChangeDao developChildOrderChangeDao;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final WmsRemoteService wmsRemoteService;
    private final SupplierDeliverBizService supplierDeliverBizService;


    /**
     * 获取原料归还列表
     *
     * @param dto:
     * @return DevelopPamphletOrderRawVo
     * @author ChenWenLong
     * @date 2023/8/17 17:53
     */
    public DevelopPamphletOrderRawDetailVo getDevelopPamphletOrderRaw(DevelopPamphletOrderRawDto dto) {
        DevelopPamphletOrderRawDetailVo developPamphletOrderRawDetailVo = new DevelopPamphletOrderRawDetailVo();
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(dto.getDevelopPamphletOrderNo());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        verifyPamphletAuth(developPamphletOrderPo);
        final List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(developPamphletOrderPo.getDevelopPamphletOrderNo(), SampleRawBizType.ACTUAL_DELIVER);

        developPamphletOrderRawDetailVo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        List<DevelopPamphletOrderRawListVo> rawList = new ArrayList<>();
        for (DevelopPamphletOrderRawPo developPamphletOrderRawPo : developPamphletOrderRawPoList) {
            DevelopPamphletOrderRawListVo developPamphletOrderRawListVo = new DevelopPamphletOrderRawListVo();
            developPamphletOrderRawListVo.setDevelopPamphletOrderRawId(developPamphletOrderRawPo.getDevelopPamphletOrderRawId());
            developPamphletOrderRawListVo.setVersion(developPamphletOrderRawPo.getVersion());
            developPamphletOrderRawListVo.setSku(developPamphletOrderRawPo.getSku());
            developPamphletOrderRawListVo.setSkuBatchCode(developPamphletOrderRawPo.getSkuBatchCode());
            developPamphletOrderRawListVo.setDeliveryCnt(developPamphletOrderRawPo.getSkuCnt());
            rawList.add(developPamphletOrderRawListVo);
        }
        developPamphletOrderRawDetailVo.setRawList(rawList);
        return developPamphletOrderRawDetailVo;
    }

    /**
     * 验证供应商
     *
     * @param developPamphletOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/5 14:14
     */
    public void verifyPamphletAuth(DevelopPamphletOrderPo developPamphletOrderPo) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }
        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(developPamphletOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }
    }

    /**
     * 验证供应商
     *
     * @param developChildOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/5 14:14
     */
    public void verifyChildAuth(DevelopChildOrderPo developChildOrderPo) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }
        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(developChildOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }
    }

    /**
     * 归还原料
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/17 18:07
     */
    @Transactional(rollbackFor = Exception.class)
    public void returnRaw(PamphletReturnRawDto dto) {
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(dto.getDevelopPamphletOrderNo());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderPo, () -> new BizException("查询不到开发子单，数据已被修改或删除，请刷新页面后重试！"));
        Assert.notBlank(developChildOrderPo.getPlatform(), () -> new BizException("开发子单:{}的平台编码为空，数据异常请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo()));

        final List<DevelopPamphletOrderRawItemDto> rawItemList = dto.getRawItemList();
        final List<Long> idList = rawItemList.stream()
                .map(DevelopPamphletOrderRawItemDto::getDevelopPamphletOrderRawId)
                .collect(Collectors.toList());

        final List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByIdList(idList);
        if (idList.size() != developPamphletOrderRawPoList.size()) {
            throw new BizException("找不到对应的原料明细，归还原料失败！");
        }

        //验证入库仓
        BooleanType warehouseVerify = wmsRemoteService.mateWarehouseCodeAndReceiveType(WmsEnum.ReceiveType.PROCESS_MATERIAL, dto.getWarehouseCode());
        Assert.isTrue(warehouseVerify.isBooleanVal(), () -> new ParamIllegalException(ScmConstant.VERIFY_WAREHOUSE_PROMPTS));
        final Map<String, WmsEnum.QcType> warehouseQcTypeMap = supplierDeliverBizService.buildWarehouseQcTypeMap(List.of(dto.getWarehouseCode()), WmsEnum.QcType.SAMPLE_CHECK);

        final Map<Long, DevelopPamphletOrderRawPo> developPamphletOrderRawPoMap = developPamphletOrderRawPoList.stream()
                .collect(Collectors.toMap(DevelopPamphletOrderRawPo::getDevelopPamphletOrderRawId, Function.identity()));

        final List<DevelopPamphletOrderRawPo> updatePoList = rawItemList.stream()
                .map(item -> {
                    final DevelopPamphletOrderRawPo developPamphletOrderRawPo = new DevelopPamphletOrderRawPo();
                    developPamphletOrderRawPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                    developPamphletOrderRawPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                    developPamphletOrderRawPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                    developPamphletOrderRawPo.setSku(item.getSku());
                    developPamphletOrderRawPo.setSkuBatchCode(item.getSkuBatchCode());
                    developPamphletOrderRawPo.setSkuCnt(item.getReturnCnt());
                    developPamphletOrderRawPo.setSampleRawBizType(SampleRawBizType.ACTUAL_DELIVER);
                    developPamphletOrderRawPo.setDevelopPamphletOrderRawId(item.getDevelopPamphletOrderRawId());
                    developPamphletOrderRawPo.setVersion(item.getVersion());

                    final DevelopPamphletOrderRawPo dbDevelopPamphletOrderRawPo = developPamphletOrderRawPoMap.get(item.getDevelopPamphletOrderRawId());
                    final int deliverCnt = dbDevelopPamphletOrderRawPo.getSkuCnt() - item.getReturnCnt();
                    if (deliverCnt < 0) {
                        throw new BizException("归还数不能超过发货数");
                    }
                    developPamphletOrderRawPo.setSkuCnt(deliverCnt);
                    return developPamphletOrderRawPo;
                }).collect(Collectors.toList());

        developPamphletOrderRawDao.updateBatchByIdVersion(updatePoList);
        long snowflakeId = idGenerateService.getSnowflakeId();
        // wms生成原料收货单
        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = SupplierDevelopConverter.rawDtoToReceiptMqDto(dto,
                developPamphletOrderPo, snowflakeId, warehouseQcTypeMap, developChildOrderPo);
        receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(dto.getDevelopPamphletOrderNo() + "-"));

        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
    }

    /**
     * 列表
     *
     * @param dto:
     * @return PageInfo<DevelopChildSearchVo>
     * @author ChenWenLong
     * @date 2023/8/21 09:47
     */
    public CommonPageResult.PageInfo<DevelopChildSearchVo> searchDevelopChild(DevelopChildSearchDto dto) {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new CommonPageResult.PageInfo<>();
        }
        dto.setAuthSupplierCode(supplierCodeList);
        return developChildBaseService.searchDevelopChild(dto);
    }

    /**
     * 详情
     *
     * @param dto:
     * @return DevelopChildDetailVo
     * @author ChenWenLong
     * @date 2023/8/21 09:59
     */
    public DevelopChildDetailVo developChildDetail(DevelopChildNoDto dto) {
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(dto.getDevelopChildOrderNo());
        if (null == developChildOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        verifyChildAuth(developChildOrderPo);
        return developChildBaseService.developChildDetail(dto);
    }

    /**
     * 版单打版审批
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/21 10:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void pamphletExamine(DevelopPamphletExamineDto dto) {
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByIdVersion(dto.getDevelopPamphletOrderId(), dto.getVersion());
        Assert.notNull(developPamphletOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        verifyChildAuth(developChildOrderPo);
        if (DevelopPamphletExamine.REFUSE_PAMPHLET.equals(dto.getDevelopPamphletExamine())) {
            if (StringUtils.isBlank(dto.getRefuseReason())) {
                throw new ParamIllegalException("请填写拒绝原因");
            }
            developPamphletOrderPo.setRefuseReason(dto.getRefuseReason());
            developPamphletOrderPo.setDevelopPamphletOrderStatus(DevelopPamphletOrderStatus.CANCEL_PAMPHLET);
            //异常处理
            developChildBaseService.createDevelopExceptionOrder(developPamphletOrderPo.getDevelopParentOrderNo(),
                    developPamphletOrderPo.getDevelopChildOrderNo(),
                    developPamphletOrderPo.getDevelopPamphletOrderNo(),
                    null,
                    developChildOrderPo);
        }

        //创建样品单
        if (DevelopPamphletExamine.AGREE_PAMPHLET.equals(dto.getDevelopPamphletExamine())) {
            developPamphletOrderPo.setDevelopPamphletOrderStatus(DevelopPamphletOrderStatus.PAMPHLET_MAKING);
            Integer developSampleNum = developPamphletOrderPo.getDevelopSampleNum();
            List<DevelopSampleOrderPo> developSampleOrderPoList = new ArrayList<>();
            for (int i = 0; i < developSampleNum; i++) {
                DevelopSampleOrderPo developSampleOrderPo = new DevelopSampleOrderPo();
                String developSampleOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_SAMPLE_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
                developSampleOrderPo.setDevelopSampleOrderNo(developSampleOrderNo);
                developSampleOrderPo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
                developSampleOrderPo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
                developSampleOrderPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_SEND_SAMPLES);
                developSampleOrderPo.setPlatform(developChildOrderPo.getPlatform());
                developSampleOrderPo.setSupplierCode(developPamphletOrderPo.getSupplierCode());
                developSampleOrderPo.setSupplierName(developPamphletOrderPo.getSupplierName());
                developSampleOrderPo.setDevelopSampleType(DevelopSampleType.NORMAL);
                developSampleOrderPoList.add(developSampleOrderPo);
            }
            developSampleOrderDao.insertBatch(developSampleOrderPoList);
        }

        developPamphletOrderPo.setPamphletDate(LocalDateTime.now());
        developPamphletOrderDao.updateByIdVersion(developPamphletOrderPo);

    }

    /**
     * 打版完成
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/21 11:18
     */
    @Transactional(rollbackFor = Exception.class)
    public void pamphletComplete(DevelopPamphletCompleteDto dto) {
        // 验证渠道价格
        if (CollectionUtils.isNotEmpty(dto.getDevelopSampleOrderDetailList())) {
            for (DevelopSampleOrderDetailDto developSampleOrderDetailDto : dto.getDevelopSampleOrderDetailList()) {
                List<DevelopOrderPriceSaveDto> developOrderPriceList = developSampleOrderDetailDto.getDevelopOrderPriceList();
                developChildBaseService.verifyDevelopOrderPrice(developOrderPriceList);
            }
        }
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByIdVersion(dto.getDevelopPamphletOrderId(), dto.getVersion());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("查找不到对应的版单信息，请联系系统管理员！"));
        DevelopPamphletOrderStatus status = developPamphletOrderPo.getDevelopPamphletOrderStatus().toCompletedPamphlet();
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderPo, () -> new BizException("查找不到对应的开发子单信息，请联系系统管理员！"));
        final DevelopChildOrderChangePo developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderChangePo, () -> new BizException("查找不到对应的开发子单信息，请联系系统管理员！"));

        //检验原料
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(developPamphletOrderPo.getDevelopPamphletOrderNo(),
                SampleRawBizType.DEMAND);
        if (CollectionUtils.isNotEmpty(developPamphletOrderRawPoList)) {
            List<String> relatedOrderNoList = developPamphletOrderRawPoList.stream()
                    .map(DevelopPamphletOrderRawPo::getDevelopPamphletOrderNo).collect(Collectors.toList());
            List<ProcessDeliveryOrderVo> processDeliveryOrderVoList = wmsRemoteService.getProcessDeliveryOrderBatch(relatedOrderNoList, WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
            Map<WmsEnum.DeliveryState, List<ProcessDeliveryOrderVo>> processDeliveryOrderVoMap = processDeliveryOrderVoList.stream()
                    .collect(Collectors.groupingBy(ProcessDeliveryOrderVo::getDeliveryState));

            //存在出库单非已签出和已取消状态
            List<ProcessDeliveryOrderVo> filteredList = processDeliveryOrderVoList.stream()
                    .filter(vo -> vo.getDeliveryState() != WmsEnum.DeliveryState.CANCELED
                            && vo.getDeliveryState() != WmsEnum.DeliveryState.SIGNED_OFF)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filteredList)) {
                throw new ParamIllegalException("该版单存在原料尚未处理，请前去进行原料收货，如未生成原料收货单请联系采购完成原料出库再收货即可！");
            }

            //已签出状态
            List<ProcessDeliveryOrderVo> processDeliveryOrderVoSignedOffList = processDeliveryOrderVoMap.get(WmsEnum.DeliveryState.SIGNED_OFF);
            if (CollectionUtils.isNotEmpty(processDeliveryOrderVoSignedOffList)) {
                List<String> relatedOrderNoSignedOffList = processDeliveryOrderVoSignedOffList.stream().map(ProcessDeliveryOrderVo::getRelatedOrderNo).collect(Collectors.toList());
                List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDevelopPamphletOrderNoListAndStatus(relatedOrderNoSignedOffList, ReceiptOrderStatus.WAIT_RECEIVE);
                Assert.isTrue(CollectionUtils.isEmpty(purchaseRawReceiptOrderPoList), () -> new ParamIllegalException("该版单存在原料尚未处理，请前去进行原料收货，如未生成原料收货单请联系采购完成原料出库再收货即可！"));
            }
        }


        verifyChildAuth(developChildOrderPo);
        final List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        //更新关联样品单信息
        if (CollectionUtils.isNotEmpty(dto.getDevelopSampleOrderDetailList())) {
            developChildBaseService.updateDevelopChildSample(dto.getDevelopSampleOrderDetailList(), developChildOrderPo, developPamphletOrderPo);
        }

        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                developSampleOrderPo.setDevelopSampleStatus(developSampleOrderPo.getDevelopSampleStatus().toAlreadySendSamples());
            }
            developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
        }
        developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.REVIEW, true, true);
        developPamphletOrderPo.setDevelopPamphletOrderStatus(status);
        developPamphletOrderPo.setFinishPamphletDate(LocalDateTime.now());
        developPamphletOrderDao.updateByIdVersion(developPamphletOrderPo);

        //生成审版单
        DevelopReviewOrderPo developReviewOrderPo = new DevelopReviewOrderPo();
        String developReviewOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_REVIEW_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        developReviewOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
        developReviewOrderPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        developReviewOrderPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        developReviewOrderPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
        developReviewOrderPo.setDevelopReviewOrderStatus(DevelopReviewOrderStatus.TO_BE_SUBMITTED_REVIEW);
        developReviewOrderPo.setDevelopReviewOrderType(DevelopReviewOrderType.SAMPLE_REVIEW);
        developReviewOrderPo.setPlatform(developChildOrderPo.getPlatform());
        developReviewOrderPo.setSupplierCode(developPamphletOrderPo.getSupplierCode());
        developReviewOrderPo.setSupplierName(developPamphletOrderPo.getSupplierName());
        developReviewOrderPo.setSpu(developChildOrderPo.getSpu());
        developReviewOrderPo.setPamphletTimes(developChildOrderPo.getPamphletTimes());
        developReviewOrderPo.setCategory(developChildOrderPo.getCategory());
        developReviewOrderPo.setCategoryId(developChildOrderPo.getCategoryId());
        developReviewOrderPo.setDevelopSampleNum(developPamphletOrderPo.getDevelopSampleNum());
        developReviewOrderDao.insert(developReviewOrderPo);

        // 生成审版单关联的样品单
        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = DevelopChildConverter.convertSamplePoToReviewSamplePo(developSampleOrderPoList, developReviewOrderPo);
        developReviewSampleOrderDao.insertBatch(developReviewSampleOrderPoList);

        // 生成版单的生产属性
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPoList)) {
            final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = DevelopChildConverter.attrPoListToDevReviewInfoList(developChildOrderAttrPoList,
                    developReviewSampleOrderPoList);
            developReviewSampleOrderInfoDao.insertBatch(developReviewSampleOrderInfoPoList);
        }
        // 更新打版完成时间
        developChildOrderChangePo.setPamphletCompletionDate(LocalDateTime.now());
        developChildOrderChangeDao.updateByIdVersion(developChildOrderChangePo);
    }

    /**
     * 获取开发子单状态栏信息
     *
     * @param :
     * @return List<DevelopChildOrderStatusVo>
     * @author ChenWenLong
     * @date 2023/8/24 15:46
     */
    public DevelopChildOrderStatusListVo developChildOrderStatus() {
        // 供应商数据权限
        final List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return new DevelopChildOrderStatusListVo();
        }
        return developChildBaseService.developChildOrderStatus(supplierCodeList);
    }
}
