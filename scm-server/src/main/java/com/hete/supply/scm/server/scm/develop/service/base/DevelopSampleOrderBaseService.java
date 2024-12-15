package com.hete.supply.scm.server.scm.develop.service.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopCompleteInfoMqDto;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.DevelopSampleOrderExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildBatchCodeCostPriceBo;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopOrderPriceVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.handler.DevelopCompleteInfoHandler;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAndBatchCodeItemBo;
import com.hete.supply.scm.server.scm.entity.bo.SkuAvgPriceBo;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleItemPo;
import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.service.base.SkuAvgPriceBaseService;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleItemDao;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/3 16:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevelopSampleOrderBaseService {

    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final DevelopSampleSettleItemDao developSampleSettleItemDao;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopChildBaseService developChildBaseService;
    private final SkuAvgPriceBaseService skuAvgPriceBaseService;
    private final SdaRemoteService sdaRemoteService;
    private final DevelopOrderPriceDao developOrderPriceDao;
    private final ChannelDao channelDao;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;

    public CommonPageResult.PageInfo<DevelopSampleOrderSearchVo> search(DevelopSampleOrderSearchDto dto) {
        if (null == this.getSearchDevelopSampleWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<DevelopSampleOrderSearchVo> pageResult = developSampleOrderDao.search(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<DevelopSampleOrderSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }
        List<String> developSampleOrderNoList = records.stream().map(DevelopSampleOrderSearchVo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<String> developChildOrderNoList = records.stream().map(DevelopSampleOrderSearchVo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        List<String> developPamphletOrderNoList = records.stream().map(DevelopSampleOrderSearchVo::getDevelopPamphletOrderNo).distinct().collect(Collectors.toList());
        List<String> skuList = records.stream().map(DevelopSampleOrderSearchVo::getSku).distinct().collect(Collectors.toList());

        //获取开发子单
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        // 获取图片
        Map<String, List<String>> developChildOrderStyleImg = developChildBaseService.getDevelopChildOrderStyleImg(developChildOrderPoList);

        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        }
        Map<String, List<DevelopSampleSettleItemPo>> developSampleSettleItemPoMap = developSampleSettleItemDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopSampleSettleItemPo::getDevelopSampleOrderNo));

        Map<String, LocalDateTime> finishPamphletDateMap = developPamphletOrderDao.getListByNoList(developPamphletOrderNoList)
                .stream().filter(w -> w.getFinishPamphletDate() != null)
                .collect(Collectors.toMap(DevelopPamphletOrderPo::getDevelopPamphletOrderNo, DevelopPamphletOrderPo::getFinishPamphletDate));

        // 查询渠道大货价格
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderNoList,
                List.of(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE));

        for (DevelopSampleOrderSearchVo record : records) {
            record.setSamplePrice(record.getSkuBatchSamplePrice());
            List<DevelopSampleSettleItemPo> developSampleSettleItemPos = developSampleSettleItemPoMap.get(record.getDevelopSampleOrderNo());
            if (CollectionUtils.isNotEmpty(developSampleSettleItemPos)) {
                record.setDevelopSampleSettleOrderNo(developSampleSettleItemPos.get(0).getDevelopSampleSettleOrderNo());
            }
            record.setFinishPamphletDate(finishPamphletDateMap.get(record.getDevelopPamphletOrderNo()));
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));
            record.setFileCodeList(developChildOrderStyleImg.get(record.getDevelopChildOrderNo()));

            // 获取样品单渠道大货价格
            List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(record.getDevelopSampleOrderNo()))
                    .collect(Collectors.toList());
            List<DevelopOrderPriceVo> developOrderPriceList = DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList);
            record.setDevelopOrderPriceList(developOrderPriceList);
        }
        return pageResult;
    }


    /**
     * 导出
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2023/8/9 17:04
     */
    public Integer getExportTotals(DevelopSampleOrderSearchDto dto) {
        Integer exportTotals = developSampleOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < DevelopSampleOrderExportVo>>
     * @author ChenWenLong
     * @date 2023/8/9 17:13
     */
    public CommonResult<ExportationListResultBo<DevelopSampleOrderExportVo>> getExportList(DevelopSampleOrderSearchDto dto) {
        ExportationListResultBo<DevelopSampleOrderExportVo> resultBo = new ExportationListResultBo<>();
        if (null == this.getSearchDevelopSampleWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<DevelopSampleOrderExportVo> exportList = developSampleOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<DevelopSampleOrderExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> developSampleOrderNoList = records.stream().map(DevelopSampleOrderExportVo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<String> skuList = records.stream().map(DevelopSampleOrderExportVo::getSku).distinct().collect(Collectors.toList());

        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        }

        Map<String, List<DevelopSampleSettleItemPo>> developSampleSettleItemPoMap = developSampleSettleItemDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopSampleSettleItemPo::getDevelopSampleOrderNo));
        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(DevelopSampleOrderExportVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        // 查询渠道大货价格
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderNoList, List.of(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE));
        Map<String, List<DevelopOrderPricePo>> developOrderPricePoMap = developOrderPricePoList.stream()
                .collect(Collectors.groupingBy(DevelopOrderPricePo::getDevelopOrderNo));
        List<Long> channelIdList = developOrderPricePoList.stream()
                .map(DevelopOrderPricePo::getChannelId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> channelNameMap = channelDao.getNameMapByIdList(channelIdList);

        for (DevelopSampleOrderExportVo record : records) {
            record.setSamplePrice(record.getSkuBatchSamplePrice());
            // 设置渠道价格
            List<DevelopOrderPricePo> developOrderPricePos = developOrderPricePoMap.get(record.getDevelopSampleOrderNo());
            if (CollectionUtils.isNotEmpty(developOrderPricePos)) {
                // 用按逗号分割
                String developOrderPrice = developOrderPricePos.stream()
                        .map(developOrderPricePo -> {
                            if (channelNameMap.containsKey(developOrderPricePo.getChannelId())) {
                                return channelNameMap.get(developOrderPricePo.getChannelId()) + ":" + developOrderPricePo.getPrice();
                            } else {
                                return "无渠道:" + developOrderPricePo.getPrice();
                            }
                        }).collect(Collectors.joining(","));
                record.setDevelopOrderPrice(developOrderPrice);
            }
            List<DevelopSampleSettleItemPo> developSampleSettleItemPos = developSampleSettleItemPoMap.get(record.getDevelopSampleOrderNo());
            if (CollectionUtils.isNotEmpty(developSampleSettleItemPos)) {
                record.setDevelopSampleSettleOrderNo(developSampleSettleItemPos.get(0).getDevelopSampleSettleOrderNo());
            }
            record.setSkuEncode(skuEncodeMap.get(record.getSku()));

            if (record.getDevelopSampleMethod() != null) {
                record.setDevelopSampleMethodName(record.getDevelopSampleMethod().getRemark());
            }

            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setHandleTimeStr(ScmTimeUtil.localDateTimeToStr(record.getHandleTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setSignTimeStr(ScmTimeUtil.localDateTimeToStr(record.getSignTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setShelvesTimeStr(ScmTimeUtil.localDateTimeToStr(record.getShelvesTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setPlatform(platCodeNameMap.get(record.getPlatform()));
        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    /**
     * 批量更新样品单状态
     *
     * @param developSampleOrderPoList:
     * @param developSampleStatus:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/9 18:10
     */
    public void updateDevelopSampleOrderPoList(List<DevelopSampleOrderPo> developSampleOrderPoList,
                                               DevelopSampleStatus developSampleStatus) {
        developSampleOrderPoList.forEach(po -> {
            po.setDevelopSampleStatus(developSampleStatus);
            po.setHandleUser(GlobalContext.getUserKey());
            po.setHandleUsername(GlobalContext.getUsername());
            po.setHandleTime(LocalDateTime.now());
        });
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
    }


    /**
     * 创建WMS的入库单
     *
     * @param developSampleOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/9 18:10
     */
    public void createReceiveOrder(List<DevelopSampleOrderPo> developSampleOrderPoList) {
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            if (DevelopSampleMethod.SALE.equals(developSampleOrderPo.getDevelopSampleMethod())) {
                receiveOrderCreateItem.setReceiveType(ReceiveType.FAST_SALE);
                receiveOrderCreateItem.setUnionKey(developSampleOrderPo.getDevelopSampleOrderNo() + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.DEVELOP_SALE.name());
            } else {
                receiveOrderCreateItem.setReceiveType(ReceiveType.SAMPLE);
                receiveOrderCreateItem.setUnionKey(developSampleOrderPo.getDevelopSampleOrderNo() + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.DEVELOP_SEAL_SAMPLE.name());
            }
            receiveOrderCreateItem.setScmBizNo(developSampleOrderPo.getDevelopSampleOrderNo());
            receiveOrderCreateItem.setSupplierCode(developSampleOrderPo.getSupplierCode());
            receiveOrderCreateItem.setSupplierName(developSampleOrderPo.getSupplierName());
            receiveOrderCreateItem.setWarehouseCode(developSampleOrderPo.getWarehouseCode());
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
            receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());

            final LocalDateTime now = LocalDateTime.now();
            receiveOrderCreateItem.setSendTime(now);
            receiveOrderCreateItem.setPlaceOrderTime(now);

            final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
            receiveOrderDetail.setBatchCode(developSampleOrderPo.getSkuBatchCode());
            receiveOrderDetail.setSkuCode(developSampleOrderPo.getSku());
            receiveOrderDetail.setSpu(developSampleOrderPo.getSpu());
            receiveOrderDetail.setPurchaseAmount(ScmConstant.DEVELOP_SAMPLE_RECEIVE_NUM);
            receiveOrderDetail.setDeliveryAmount(ScmConstant.DEVELOP_SAMPLE_RECEIVE_NUM);
            Assert.notBlank(developSampleOrderPo.getPlatform(), () -> new BizException("样品单:{}的平台编码为空，数据异常请联系系统管理员！", developSampleOrderPo.getDevelopSampleOrderNo()));
            receiveOrderDetail.setPlatCode(developSampleOrderPo.getPlatform());
            receiveOrderCreateItem.setDetailList(Collections.singletonList(receiveOrderDetail));

            final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
            receiveOrderCreateMqDto.setReceiveOrderCreateItemList(Collections.singletonList(receiveOrderCreateItem));
            receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(developSampleOrderPo.getDevelopSampleOrderNo() + "-"));

            consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
        }
    }

    /**
     * 重新计算样品单的批次码重复求平均值
     *
     * @param boList:
     * @return List<UpdateBatchCodeCostPrice>
     * @author ChenWenLong
     * @date 2024/1/30 18:06
     */
    public List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> calculatedSkuBatchSamplePrice(List<DevelopChildBatchCodeCostPriceBo> boList) {
        if (CollectionUtils.isEmpty(boList)) {
            return Collections.emptyList();
        }
        log.info("重新计算样品单的批次码重复求平均值，原始列表={}", JacksonUtil.parse2Str(boList));

        Map<String, List<DevelopChildBatchCodeCostPriceBo>> groupedByBatchCode = boList.stream()
                .collect(Collectors.groupingBy(DevelopChildBatchCodeCostPriceBo::getSkuBatchCode));

        List<UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice> averagedList = groupedByBatchCode.entrySet().stream()
                .map(entry -> {
                    String batchCode = entry.getKey();
                    List<DevelopChildBatchCodeCostPriceBo> pricesList = entry.getValue();
                    String sku = pricesList.get(0).getSku();

                    // 检测是否存在重复的批次码
                    BigDecimal total = pricesList.stream()
                            .map(DevelopChildBatchCodeCostPriceBo::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    SkuAndBatchCodeBo skuAndBatchCodeBo = new SkuAndBatchCodeBo();
                    SkuAndBatchCodeItemBo skuAndBatchCodeItemBo = new SkuAndBatchCodeItemBo();
                    skuAndBatchCodeItemBo.setSku(sku);
                    skuAndBatchCodeItemBo.setSkuBatchCode(batchCode);
                    skuAndBatchCodeItemBo.setAccrueCnt(pricesList.size());
                    skuAndBatchCodeItemBo.setAccruePrice(total);
                    skuAndBatchCodeBo.setSkuAndBatchCodeItemBoList(List.of(skuAndBatchCodeItemBo));
                    skuAndBatchCodeBo.setSkuAvgPriceBizType(SkuAvgPriceBizType.DEVELOP_SAMPLE_ORDER);
                    List<SkuAvgPriceBo> skuAvgPriceBoList = skuAvgPriceBaseService.getSkuAvgPrice(skuAndBatchCodeBo);
                    Map<String, SkuAvgPriceBo> skuAvgPriceBoMap = skuAvgPriceBoList.stream()
                            .collect(Collectors.toMap(SkuAvgPriceBo::getSkuBatchCode, Function.identity()));
                    if (!skuAvgPriceBoMap.containsKey(batchCode)) {
                        throw new BizException("批次码{}获取不到平均价格，请联系管理员！", batchCode);
                    }
                    BigDecimal price = skuAvgPriceBoMap.get(batchCode).getAvgPrice();

                    UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice updateBatchCodeCostPrice = new UpdateBatchCodePriceDto.UpdateBatchCodeCostPrice();
                    updateBatchCodeCostPrice.setBatchCode(batchCode);
                    updateBatchCodeCostPrice.setPrice(price);
                    return updateBatchCodeCostPrice;

                })
                .collect(Collectors.toList());

        log.info("重新计算样品单的批次码重复求平均值，去重后的平均值列表={}", JacksonUtil.parse2Str(averagedList));
        return averagedList;

    }

    /**
     * 样品单跟单闪售PLM创建sku
     *
     * @param developSampleOrderPoList:
     * @param developSampleMethod:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/25 16:04
     */
    public void submitHandlePushMq(List<DevelopSampleOrderPo> developSampleOrderPoList, DevelopSampleMethod developSampleMethod) {

        log.info("样品单跟单闪售PLM创建sku。PO={}", JacksonUtil.parse2Str(developSampleOrderPoList));
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            return;
        }
        List<String> developChildOrderNoList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        if (CollectionUtils.isNotEmpty(developChildOrderNoList) && developChildOrderNoList.size() != developChildOrderPoList.size()) {
            log.info("存在样品单找不到对应开发子单的数据异常,developChildOrderPo={}", developChildOrderPoList);
            throw new BizException("存在样品单找不到对应开发子单的数据异常，请联系管理员！");
        }

        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            DevelopChildOrderPo developChildOrderPo = developChildOrderPoList.stream()
                    .filter(childOrderPo -> childOrderPo.getDevelopChildOrderNo().equals(developSampleOrderPo.getDevelopChildOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (null == developChildOrderPo) {
                throw new BizException("样品单号:{}关联开发子单号:{}找不到对应开发子单的数据异常，请联系管理员！", developSampleOrderPo.getDevelopSampleOrderNo(),
                        developSampleOrderPo.getDevelopChildOrderNo());
            }
            //推送PLM
            final DevelopCompleteInfoMqDto developCompleteInfoMqDto = new DevelopCompleteInfoMqDto();
            developCompleteInfoMqDto.setKey(developSampleOrderPo.getDevelopSampleOrderNo());
            developCompleteInfoMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developCompleteInfoMqDto.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
            developCompleteInfoMqDto.setDevelopSampleDestination(developSampleMethod.getDevelopSampleDestination());
            developCompleteInfoMqDto.setUserKey(GlobalContext.getUserKey());
            developCompleteInfoMqDto.setUsername(GlobalContext.getUsername());
            developCompleteInfoMqDto.setPlatCode(developChildOrderPo.getPlatform());
            developCompleteInfoMqDto.setCategoryId(developChildOrderPo.getCategoryId());

            String title = developChildOrderPo.getDevelopParentOrderNo() + developChildOrderPo.getDevelopChildOrderNo() + developSampleOrderPo.getDevelopSampleOrderNo() + developSampleOrderPo.getDevelopSampleMethod().getRemark();

            developCompleteInfoMqDto.setTitleCn(title);
            developCompleteInfoMqDto.setDescription(title);
            consistencySendMqService.execSendMq(DevelopCompleteInfoHandler.class, developCompleteInfoMqDto);
        }

    }

    /**
     * 样品单列表和导出检索条件
     *
     * @param dto:
     * @return DevelopSampleOrderSearchDto
     * @author ChenWenLong
     * @date 2024/4/12 09:56
     */
    public DevelopSampleOrderSearchDto getSearchDevelopSampleWhere(DevelopSampleOrderSearchDto dto) {
        //单个产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
                if (CollectionUtils.isEmpty(dto.getSkuList())) {
                    return null;
                }
            }
        }

        // 产品名称批量查询
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }
        return dto;
    }

    /**
     * 获取开发子单关联审版单时克重最高的封样入库的审版样品单
     *
     * @param developChildOrderNoList:开发子单号
     * @return Map<String, DevelopReviewSampleOrderPo>:开发子单号对应克重最高的封样入库的审版样品单
     * @author ChenWenLong
     * @date 2024/8/21 11:25
     */
    public Map<String, DevelopReviewSampleOrderPo> getSealSampleByChildOrderNoList(List<String> developChildOrderNoList) {
        if (CollectionUtils.isEmpty(developChildOrderNoList)) {
            return Collections.emptyMap();
        }

        // 获取开发子单关联常规封样的样品单
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByChildOrderNoListAndType(developChildOrderNoList,
                DevelopSampleMethod.SEAL_SAMPLE,
                List.of(DevelopSampleType.NORMAL));

        List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopSampleOrderNo).collect(Collectors.toList());

        // 查询开发审版关联样品单
        List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);

        Map<String, DevelopReviewSampleOrderPo> resultMap = new HashMap<>();
        for (String developChildOrderNo : developChildOrderNoList) {
            // 获取开发子单关联审版单时克重最高的封样入库的样品单
            DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderPoList.stream()
                    .filter(po -> developChildOrderNo.equals(po.getDevelopChildOrderNo()))
                    .max(Comparator.comparing(DevelopReviewSampleOrderPo::getGramWeight))
                    .orElse(null);
            if (null == developReviewSampleOrderPo) {
                continue;
            }
            resultMap.put(developChildOrderNo, developReviewSampleOrderPo);
        }

        log.info("获取开发子单关联审版单时克重最高的封样入库Map=>{}", resultMap);
        return resultMap;

    }

}
