package com.hete.supply.scm.server.scm.sample.converter;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SampleNoAndStatusVo;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.server.scm.entity.dto.SimpleReturnItemDto;
import com.hete.supply.scm.server.scm.entity.vo.RawSampleItemVo;
import com.hete.supply.scm.server.scm.sample.entity.dto.*;
import com.hete.supply.scm.server.scm.sample.entity.po.*;
import com.hete.supply.scm.server.scm.sample.entity.vo.*;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleProcessDescDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverDetailVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverItemVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleSplitItemVo;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/7 11:39
 */
public class SampleConverter {
    public static SampleParentOrderPo createDtoToPo(SampleCreateDto dto, SampleOrderStatus targetStatus) {
        final SampleParentOrderPo sampleParentOrderPo = new SampleParentOrderPo();
        sampleParentOrderPo.setSampleOrderStatus(targetStatus);
        sampleParentOrderPo.setSampleDevType(dto.getSampleDevType());
        sampleParentOrderPo.setCategoryName(dto.getCategoryName());
        sampleParentOrderPo.setSpu(dto.getSpu());
        sampleParentOrderPo.setPlatform(dto.getPlatform());
        sampleParentOrderPo.setWarehouseCode(dto.getWarehouseCode());
        sampleParentOrderPo.setWarehouseName(dto.getWarehouseName());
        sampleParentOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        sampleParentOrderPo.setDeliverDate(dto.getDeliverDate());
        sampleParentOrderPo.setPurchasePredictPrice(dto.getPurchasePredictPrice());
        sampleParentOrderPo.setIsFirstOrder(dto.getIsFirstOrder());
        sampleParentOrderPo.setIsUrgentOrder(dto.getIsUrgentOrder());
        sampleParentOrderPo.setSourceMaterial(dto.getSourceMaterial());
        sampleParentOrderPo.setSampleDescribe(dto.getSampleDescribe());
        sampleParentOrderPo.setDefectiveSampleChildOrderNo(dto.getDefectiveSampleChildOrderNo());
        sampleParentOrderPo.setIsSample(dto.getIsSample());
        sampleParentOrderPo.setRawWarehouseCode(dto.getRawWarehouseCode());
        sampleParentOrderPo.setRawWarehouseName(dto.getRawWarehouseName());

        return sampleParentOrderPo;
    }

    public static SampleDetailVo poToDetailVo(SampleParentOrderPo sampleParentOrderPo,
                                              List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList,
                                              List<SampleChildOrderPo> sampleChildOrderPoList,
                                              List<SampleParentOrderRawPo> sampleParentOrderRawPoList) {
        final SampleDetailVo sampleDetailVo = new SampleDetailVo();
        sampleDetailVo.setSampleParentOrderId(sampleParentOrderPo.getSampleParentOrderId());
        sampleDetailVo.setVersion(sampleParentOrderPo.getVersion());
        sampleDetailVo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
        sampleDetailVo.setSampleOrderStatus(sampleParentOrderPo.getSampleOrderStatus());
        sampleDetailVo.setSpu(sampleParentOrderPo.getSpu());
        sampleDetailVo.setWarehouseName(sampleParentOrderPo.getWarehouseName());
        sampleDetailVo.setWarehouseCode(sampleParentOrderPo.getWarehouseCode());
        sampleDetailVo.setWarehouseTypeList(FormatStringUtil.string2List(sampleParentOrderPo.getWarehouseTypes(), ","));
        sampleDetailVo.setDeliverDate(sampleParentOrderPo.getDeliverDate());
        sampleDetailVo.setPlatform(sampleParentOrderPo.getPlatform());
        sampleDetailVo.setSupplySampleType(sampleParentOrderPo.getSupplySampleType());
        sampleDetailVo.setSampleDevType(sampleParentOrderPo.getSampleDevType());
        sampleDetailVo.setPurchasePredictPrice(sampleParentOrderPo.getPurchasePredictPrice());
        sampleDetailVo.setSampleDescribe(sampleParentOrderPo.getSampleDescribe());
        sampleDetailVo.setDefectiveSampleChildOrderNo(sampleParentOrderPo.getDefectiveSampleChildOrderNo());
        sampleDetailVo.setCategoryName(sampleParentOrderPo.getCategoryName());
        sampleDetailVo.setIsFirstOrder(sampleParentOrderPo.getIsFirstOrder());
        sampleDetailVo.setIsUrgentOrder(sampleParentOrderPo.getIsUrgentOrder());
        sampleDetailVo.setIsNormalOrder(sampleParentOrderPo.getIsNormalOrder());
        sampleDetailVo.setSourceMaterial(sampleParentOrderPo.getSourceMaterial());
        sampleDetailVo.setIsSample(sampleParentOrderPo.getIsSample());

        final List<SampleRawVo> sampleRawList = Optional.ofNullable(sampleParentOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(rawParentPo -> {
                    final SampleRawVo sampleRawVo = new SampleRawVo();
                    sampleRawVo.setSampleOrderRawId(rawParentPo.getSampleParentOrderRawId());
                    sampleRawVo.setVersion(rawParentPo.getVersion());
                    sampleRawVo.setSku(rawParentPo.getSku());
                    sampleRawVo.setDeliveryCnt(rawParentPo.getDeliveryCnt());

                    return sampleRawVo;
                }).collect(Collectors.toList());
        sampleDetailVo.setSampleRawList(sampleRawList);

        List<SampleParentOrderInfoVo> sampleParentOrderInfoList = infoPoListToVoList(sampleParentOrderInfoPoList);

        sampleDetailVo.setSampleParentOrderInfoList(sampleParentOrderInfoList);

        sampleDetailVo.setSampleChildOrderList(sampleChildOrderPoListToVo(sampleChildOrderPoList));


        return sampleDetailVo;
    }

    public static List<SampleChildOrderPo> splitDtoToChildOrderList(SampleSplitDto dto, int childOrderNoIndex,
                                                                    SampleParentOrderPo sampleParentOrderPo,
                                                                    Map<String, String> supplierNameMap) {

        List<SampleChildOrderPo> sampleChildOrderList = new ArrayList<>();
        for (SampleSplitDto.SampleSplitItem item : dto.getSampleSplitItemList()) {
            SampleChildOrderPo sampleChildOrderPo = new SampleChildOrderPo();
            final String indexStr = StringUtil.toTwoDigitFormat(++childOrderNoIndex);
            sampleChildOrderPo.setSampleParentOrderNo(dto.getSampleParentOrderNo());
            sampleChildOrderPo.setSampleChildOrderNo(dto.getSampleParentOrderNo() + "-" + indexStr);
            sampleChildOrderPo.setSampleOrderStatus(SampleOrderStatus.WAIT_TYPESET);
            sampleChildOrderPo.setSpu(sampleParentOrderPo.getSpu());
            sampleChildOrderPo.setSupplierCode(item.getSupplierCode());
            sampleChildOrderPo.setSupplierName(supplierNameMap.get(item.getSupplierCode()));
            sampleChildOrderPo.setPurchaseCnt(item.getPurchaseCnt());
            sampleChildOrderPo.setPurchasePredictPrice(item.getPurchasePredictPrice());
            sampleChildOrderPo.setWarehouseName(item.getWarehouseName());
            sampleChildOrderPo.setWarehouseCode(item.getWarehouseCode());
            sampleChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(item.getWarehouseTypeList())
                    .orElse(new ArrayList<>())));
            sampleChildOrderPo.setDeliverDate(item.getDeliverDate());
            sampleChildOrderPo.setDemandDescribe(item.getDemandDescribe());
            sampleChildOrderPo.setPlatform(sampleParentOrderPo.getPlatform());
            sampleChildOrderPo.setSampleDescribe(sampleParentOrderPo.getSampleDescribe());
            sampleChildOrderPo.setSupplierProduction(item.getSupplierProduction());
            sampleChildOrderPo.setSampleDevType(sampleParentOrderPo.getSampleDevType());
            sampleChildOrderPo.setRawWarehouseCode(sampleParentOrderPo.getRawWarehouseCode());
            sampleChildOrderPo.setRawWarehouseName(sampleParentOrderPo.getRawWarehouseName());

            sampleChildOrderList.add(sampleChildOrderPo);
        }
        return sampleChildOrderList;

    }

    public static List<SampleChildOrderPo> specialItemDtoToChildOrderList(List<SampleSpecialItemDto> sampleSpecialItemList,
                                                                          SampleParentOrderPo sampleParentOrderPo,
                                                                          Map<String, String> supplierNameMap) {
        List<SampleChildOrderPo> sampleChildOrderList = new ArrayList<>();

        int childOrderNoIndex = 0;
        for (SampleSpecialItemDto item : sampleSpecialItemList) {
            SampleChildOrderPo sampleChildOrderPo = new SampleChildOrderPo();
            final String indexStr = StringUtil.toTwoDigitFormat(++childOrderNoIndex);
            sampleChildOrderPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
            sampleChildOrderPo.setSampleChildOrderNo(sampleParentOrderPo.getSampleParentOrderNo() + "-" + indexStr);
            sampleChildOrderPo.setSampleOrderStatus(SampleOrderStatus.WAIT_SAMPLE);
            sampleChildOrderPo.setSpu(sampleParentOrderPo.getSpu());
            sampleChildOrderPo.setSupplierCode(item.getSupplierCode());
            sampleChildOrderPo.setSupplierName(supplierNameMap.get(item.getSupplierCode()));
            sampleChildOrderPo.setPurchaseCnt(item.getPurchaseCnt());
            sampleChildOrderPo.setWarehouseName(sampleParentOrderPo.getWarehouseName());
            sampleChildOrderPo.setWarehouseCode(sampleParentOrderPo.getWarehouseCode());
            sampleChildOrderPo.setWarehouseTypes(sampleParentOrderPo.getWarehouseTypes());
            sampleChildOrderPo.setDeliverDate(sampleParentOrderPo.getDeliverDate());
            sampleChildOrderPo.setPlatform(sampleParentOrderPo.getPlatform());
            sampleChildOrderPo.setSampleDescribe(sampleParentOrderPo.getSampleDescribe());
            sampleChildOrderPo.setSupplierProduction(item.getSupplierProduction());
            sampleChildOrderPo.setProofingPrice(item.getProofingPrice());
            sampleChildOrderPo.setCostPrice(item.getCostPrice());
            sampleChildOrderPo.setReceiptCnt(item.getPurchaseCnt());
            sampleChildOrderPo.setSampleDevType(sampleParentOrderPo.getSampleDevType());

            sampleChildOrderList.add(sampleChildOrderPo);
        }

        return sampleChildOrderList;
    }

    public static SampleParentOrderChangePo parentPoToChangePo(SampleParentOrderPo sampleParentOrderPo) {
        final SampleParentOrderChangePo sampleParentOrderChangePo = new SampleParentOrderChangePo();
        sampleParentOrderChangePo.setSampleParentOrderId(sampleParentOrderPo.getSampleParentOrderId());
        sampleParentOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
        sampleParentOrderChangePo.setPlaceOrderUser(GlobalContext.getUserKey());
        sampleParentOrderChangePo.setPlaceOrderUsername(GlobalContext.getUsername());

        return sampleParentOrderChangePo;
    }

    public static List<SampleChildOrderChangePo> childOrderToChildOrderChangeList(List<SampleChildOrderPo> sampleChildOrderPoList) {
        return sampleChildOrderPoList.stream()
                .map(po -> {
                    SampleChildOrderChangePo sampleChildOrderChangePo = new SampleChildOrderChangePo();
                    sampleChildOrderChangePo.setSampleChildOrderId(po.getSampleChildOrderId());
                    sampleChildOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
                    sampleChildOrderChangePo.setPlaceOrderUser(GlobalContext.getUserKey());
                    sampleChildOrderChangePo.setPlaceOrderUsername(GlobalContext.getUsername());
                    return sampleChildOrderChangePo;
                }).collect(Collectors.toList());
    }

    public static List<SampleParentOrderInfoPo> infoListToPoList(List<SampleParentOrderInfoDto> sampleParentOrderInfoList, String sampleParentOrderNo) {
        return Optional.ofNullable(sampleParentOrderInfoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(dto -> {
                    final SampleParentOrderInfoPo sampleParentOrderInfoPo = new SampleParentOrderInfoPo();
                    sampleParentOrderInfoPo.setSampleParentOrderNo(sampleParentOrderNo);
                    sampleParentOrderInfoPo.setSampleInfoKey(dto.getSampleInfoKey());
                    sampleParentOrderInfoPo.setSampleInfoValue(dto.getSampleInfoValue());
                    return sampleParentOrderInfoPo;
                }).collect(Collectors.toList());
    }

    public static void sampleSplitItemDtoToChildOrderList(List<SampleChildOrderPo> sampleChildOrderPoList,
                                                          List<SampleChildOrderDto> sampleSplitItemList,
                                                          Map<String, String> supplierNameMap) {
        final Map<Long, SampleChildOrderPo> sampleChildIdMap = sampleChildOrderPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderPo::getSampleChildOrderId, Function.identity()));
        sampleSplitItemList.forEach(dto -> {
            final SampleChildOrderPo sampleChildOrderPo = sampleChildIdMap.get(dto.getSampleChildOrderId());
            sampleChildOrderPo.setSampleChildOrderId(dto.getSampleChildOrderId());
            sampleChildOrderPo.setVersion(dto.getVersion());
            sampleChildOrderPo.setSupplierCode(dto.getSupplierCode());
            sampleChildOrderPo.setSupplierName(supplierNameMap.get(dto.getSupplierCode()));
            sampleChildOrderPo.setPurchaseCnt(dto.getPurchaseCnt());
            sampleChildOrderPo.setPurchasePredictPrice(dto.getPurchasePredictPrice());
            sampleChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
            sampleChildOrderPo.setWarehouseName(dto.getWarehouseName());
            sampleChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                    .orElse(new ArrayList<>())));
            sampleChildOrderPo.setDeliverDate(dto.getDeliverDate());
            sampleChildOrderPo.setSupplierProduction(dto.getSupplierProduction());
            sampleChildOrderPo.setDemandDescribe(dto.getDemandDescribe());
        });
    }

    public static SamplePurchaseDetailVo poToPurchaseDetailVo(SampleChildOrderPo sampleChildOrderPo,
                                                              SampleParentOrderPo sampleParentOrderPo,
                                                              List<SampleChildOrderInfoPo> sampleChildInfoPoList,
                                                              List<SampleChildOrderPo> sampleChildOrderPoList) {
        final List<SampleChildOrderInfoVo> sampleChildOrderInfoList = childInfoPoListToVoList(sampleChildInfoPoList);

        final SamplePurchaseDetailVo samplePurchaseDetailVo = new SamplePurchaseDetailVo();
        samplePurchaseDetailVo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        samplePurchaseDetailVo.setVersion(sampleChildOrderPo.getVersion());
        samplePurchaseDetailVo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
        samplePurchaseDetailVo.setSampleOrderStatus(sampleParentOrderPo.getSampleOrderStatus());
        samplePurchaseDetailVo.setSampleChildOrderStatus(sampleChildOrderPo.getSampleOrderStatus());
        samplePurchaseDetailVo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
        samplePurchaseDetailVo.setSpu(sampleChildOrderPo.getSpu());
        samplePurchaseDetailVo.setWarehouseCode(sampleChildOrderPo.getWarehouseCode());
        samplePurchaseDetailVo.setWarehouseName(sampleChildOrderPo.getWarehouseName());
        samplePurchaseDetailVo.setWarehouseTypeList(FormatStringUtil.string2List(sampleChildOrderPo.getWarehouseTypes(), ","));
        samplePurchaseDetailVo.setSupplierCode(sampleChildOrderPo.getSupplierCode());
        samplePurchaseDetailVo.setSupplierName(sampleChildOrderPo.getSupplierName());
        samplePurchaseDetailVo.setDeliverDate(sampleChildOrderPo.getDeliverDate());
        samplePurchaseDetailVo.setPlatform(sampleChildOrderPo.getPlatform());
        samplePurchaseDetailVo.setPurchaseCnt(sampleChildOrderPo.getPurchaseCnt());
        samplePurchaseDetailVo.setReceiptCnt(sampleChildOrderPo.getReceiptCnt());
        samplePurchaseDetailVo.setSku(sampleChildOrderPo.getSku());
        samplePurchaseDetailVo.setSkuBatchCode(sampleChildOrderPo.getSkuBatchCode());
        samplePurchaseDetailVo.setPurchasePredictPrice(sampleChildOrderPo.getPurchasePredictPrice());
        samplePurchaseDetailVo.setProofingPrice(sampleChildOrderPo.getProofingPrice());
        samplePurchaseDetailVo.setCostPrice(sampleChildOrderPo.getCostPrice());
        samplePurchaseDetailVo.setSampleDescribe(sampleChildOrderPo.getSampleDescribe());
        samplePurchaseDetailVo.setSampleImprove(sampleChildOrderPo.getSampleImprove());
        samplePurchaseDetailVo.setDemandDescribe(sampleChildOrderPo.getDemandDescribe());
        samplePurchaseDetailVo.setSampleChildOrderInfoList(sampleChildOrderInfoList);
        samplePurchaseDetailVo.setSampleChildOrderList(sampleChildOrderPoListToVo(sampleChildOrderPoList));
        samplePurchaseDetailVo.setSupplierProduction(sampleChildOrderPo.getSupplierProduction());
        samplePurchaseDetailVo.setSourceMaterial(sampleParentOrderPo.getSourceMaterial());
        samplePurchaseDetailVo.setCategoryName(sampleParentOrderPo.getCategoryName());
        samplePurchaseDetailVo.setSampleDevType(sampleParentOrderPo.getSampleDevType());
        samplePurchaseDetailVo.setRawWarehouseCode(sampleChildOrderPo.getRawWarehouseCode());
        samplePurchaseDetailVo.setRawWarehouseName(sampleChildOrderPo.getRawWarehouseName());

        return samplePurchaseDetailVo;
    }

    private static List<SampleChildOrderVo> sampleChildOrderPoListToVo(List<SampleChildOrderPo> sampleChildOrderPoList) {
        return sampleChildOrderPoList.stream()
                .map(po -> {
                    final SampleChildOrderVo sampleChildOrderVo = new SampleChildOrderVo();
                    sampleChildOrderVo.setSampleChildOrderId(po.getSampleChildOrderId());
                    sampleChildOrderVo.setVersion(po.getVersion());
                    sampleChildOrderVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleChildOrderVo.setSupplierCode(po.getSupplierCode());
                    sampleChildOrderVo.setSupplierName(po.getSupplierName());
                    sampleChildOrderVo.setTotalSettlePrice(po.getSettlePrice());
                    sampleChildOrderVo.setPurchaseCnt(po.getPurchaseCnt());
                    sampleChildOrderVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    sampleChildOrderVo.setSpu(po.getSpu());
                    sampleChildOrderVo.setSku(po.getSku());
                    sampleChildOrderVo.setProofingPrice(po.getProofingPrice());
                    sampleChildOrderVo.setCostPrice(po.getCostPrice());
                    sampleChildOrderVo.setSupplierProduction(po.getSupplierProduction());
                    return sampleChildOrderVo;

                }).collect(Collectors.toList());
    }

    public static List<SampleParentOrderInfoVo> infoPoListToVoList(List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList) {
        return Optional.ofNullable(sampleParentOrderInfoPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(po -> {
                    final SampleParentOrderInfoVo sampleParentOrderInfoVo = new SampleParentOrderInfoVo();
                    sampleParentOrderInfoVo.setId(po.getSampleParentOrderInfoId());
                    sampleParentOrderInfoVo.setVersion(po.getVersion());
                    sampleParentOrderInfoVo.setSampleInfoKey(po.getSampleInfoKey());
                    sampleParentOrderInfoVo.setSampleInfoValue(po.getSampleInfoValue());
                    sampleParentOrderInfoVo.setSampleParentOrderNo(po.getSampleParentOrderNo());
                    return sampleParentOrderInfoVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleSplitItemVo> childPoToItemVo(List<SampleChildOrderPo> poList) {
        return Optional.ofNullable(poList)
                .orElse(new ArrayList<>())
                .stream()
                .map(po -> {
                    final SampleSplitItemVo sampleSplitItemVo = new SampleSplitItemVo();
                    sampleSplitItemVo.setSupplierCode(po.getSupplierCode());
                    sampleSplitItemVo.setSupplierName(po.getSupplierName());
                    sampleSplitItemVo.setPurchaseCnt(po.getPurchaseCnt());
                    sampleSplitItemVo.setPurchasePredictPrice(po.getPurchasePredictPrice());
                    sampleSplitItemVo.setWarehouseCode(po.getWarehouseCode());
                    sampleSplitItemVo.setWarehouseName(po.getWarehouseName());
                    sampleSplitItemVo.setWarehouseTypeList(FormatStringUtil.string2List(po.getWarehouseTypes(), ","));
                    sampleSplitItemVo.setDeliverDate(po.getDeliverDate());
                    sampleSplitItemVo.setDemandDescribe(po.getDemandDescribe());
                    sampleSplitItemVo.setSampleChildOrderId(po.getSampleChildOrderId());
                    sampleSplitItemVo.setVersion(po.getVersion());
                    sampleSplitItemVo.setSupplierProduction(po.getSupplierProduction());
                    return sampleSplitItemVo;
                }).collect(Collectors.toList());
    }


    public static List<SampleReceiptOrderItemPo> receiptItemDtoToPo(List<SampleReceiptOrderItemDto> sampleReceiptOrderItemList) {

        return sampleReceiptOrderItemList.stream()
                .map(dto -> {
                    final SampleReceiptOrderItemPo sampleReceiptOrderItemPo = new SampleReceiptOrderItemPo();
                    sampleReceiptOrderItemPo.setSampleReceiptOrderItemId(dto.getSampleReceiptOrderItemId());
                    sampleReceiptOrderItemPo.setVersion(dto.getVersion());
                    sampleReceiptOrderItemPo.setReceiptCnt(dto.getReceiptCnt());
                    return sampleReceiptOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static SampleDeliverDetailVo deliverPoToDetailVo(SampleDeliverOrderPo sampleDeliverOrderPo, List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList) {
        final SampleDeliverDetailVo sampleDeliverDetailVo = new SampleDeliverDetailVo();
        sampleDeliverDetailVo.setSampleDeliverOrderId(sampleDeliverOrderPo.getSampleDeliverOrderId());
        sampleDeliverDetailVo.setVersion(sampleDeliverOrderPo.getVersion());
        sampleDeliverDetailVo.setSampleDeliverOrderNo(sampleDeliverOrderPo.getSampleDeliverOrderNo());
        sampleDeliverDetailVo.setSampleDeliverOrderStatus(sampleDeliverOrderPo.getSampleDeliverOrderStatus());
        sampleDeliverDetailVo.setLogistics(sampleDeliverOrderPo.getLogistics());
        sampleDeliverDetailVo.setTrackingNo(sampleDeliverOrderPo.getTrackingNo());
        sampleDeliverDetailVo.setSupplierCode(sampleDeliverOrderPo.getSupplierCode());
        sampleDeliverDetailVo.setSupplierName(sampleDeliverOrderPo.getSupplierName());
        sampleDeliverDetailVo.setWarehouseCode(sampleDeliverOrderPo.getWarehouseCode());
        sampleDeliverDetailVo.setWarehouseName(sampleDeliverOrderPo.getWarehouseName());
        sampleDeliverDetailVo.setWarehouseTypeList(FormatStringUtil.string2List(sampleDeliverOrderPo.getWarehouseTypes(), ","));

        final List<SampleDeliverItemVo> sampleDeliverItemList = sampleDeliverOrderItemPoList.stream()
                .map(po -> {
                    final SampleDeliverItemVo SampleDeliverItemVo = new SampleDeliverItemVo();
                    SampleDeliverItemVo.setSpu(po.getSpu());
                    SampleDeliverItemVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    SampleDeliverItemVo.setDeliverCnt(po.getDeliverCnt());
                    return SampleDeliverItemVo;
                }).collect(Collectors.toList());

        sampleDeliverDetailVo.setSampleDeliverItemList(sampleDeliverItemList);


        return sampleDeliverDetailVo;
    }

    public static SampleReturnOrderPo returnDtoToReturnPo(SampleReturnCreateDto dto) {
        final SampleReturnOrderPo sampleReturnOrderPo = new SampleReturnOrderPo();
        sampleReturnOrderPo.setReturnOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);
        sampleReturnOrderPo.setLogistics(dto.getLogistics());
        sampleReturnOrderPo.setTrackingNo(dto.getTrackingNo());
        sampleReturnOrderPo.setSupplierCode(dto.getSupplierCode());
        sampleReturnOrderPo.setSupplierName(dto.getSupplierName());

        return sampleReturnOrderPo;
    }

    public static List<SampleReturnOrderItemPo> childOrderPoListToReturnItemList(List<SampleChildOrderPo> sampleChildOrderPoList,
                                                                                 List<SimpleReturnItemDto> sampleReturnItemList,
                                                                                 String sampleReturnOrderNo) {
        final Map<String, SampleChildOrderPo> sampleChildOrderPoMap = sampleChildOrderPoList.stream()
                .collect(Collectors.toMap(SampleChildOrderPo::getSampleChildOrderNo, Function.identity()));

        return sampleReturnItemList.stream()
                .map(dto -> {
                    final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderPoMap.get(dto.getSampleChildOrderNo());
                    final SampleReturnOrderItemPo sampleReturnOrderItemPo = new SampleReturnOrderItemPo();
                    sampleReturnOrderItemPo.setSampleReturnOrderNo(sampleReturnOrderNo);
                    sampleReturnOrderItemPo.setSpu(sampleChildOrderPo.getSpu());
                    sampleReturnOrderItemPo.setSampleChildOrderNo(dto.getSampleChildOrderNo());
                    sampleReturnOrderItemPo.setReturnOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);
                    sampleReturnOrderItemPo.setReturnCnt(dto.getReturnCnt());
                    return sampleReturnOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderInfoPo> childOrderNoListToInfoList(List<String> sampleChildOrderNoList,
                                                                          List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList) {
        List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = new ArrayList<>();
        for (String sampleChildOrderNo : sampleChildOrderNoList) {
            final List<SampleChildOrderInfoPo> extendsList = sampleParentOrderInfoPoList.stream()
                    .map(po -> {
                        final SampleChildOrderInfoPo sampleChildOrderInfoPo = new SampleChildOrderInfoPo();
                        sampleChildOrderInfoPo.setSampleChildOrderNo(sampleChildOrderNo);
                        sampleChildOrderInfoPo.setSampleInfoKey(po.getSampleInfoKey());
                        sampleChildOrderInfoPo.setSampleInfoValue(po.getSampleInfoValue());
                        return sampleChildOrderInfoPo;
                    }).collect(Collectors.toList());
            sampleChildOrderInfoPoList.addAll(extendsList);
        }
        return sampleChildOrderInfoPoList;
    }

    public static List<SampleChildOrderInfoVo> childInfoPoListToVoList(List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList) {
        return Optional.ofNullable(sampleChildOrderInfoPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(po -> {
                    final SampleChildOrderInfoVo sampleChildOrderInfoVo = new SampleChildOrderInfoVo();
                    sampleChildOrderInfoVo.setId(po.getSampleChildOrderInfoId());
                    sampleChildOrderInfoVo.setVersion(po.getVersion());
                    sampleChildOrderInfoVo.setSampleInfoKey(po.getSampleInfoKey());
                    sampleChildOrderInfoVo.setSampleInfoValue(po.getSampleInfoValue());
                    sampleChildOrderInfoVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    return sampleChildOrderInfoVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderInfoPo> childOrderInfoDtoListToPo(List<SampleChildOrderInfoDto> sampleChildOrderInfoList,
                                                                         String sampleChildOrderNo) {
        return Optional.ofNullable(sampleChildOrderInfoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> {
                    final SampleChildOrderInfoPo sampleChildOrderInfoPo = new SampleChildOrderInfoPo();
                    sampleChildOrderInfoPo.setSampleChildOrderInfoId(dto.getId());
                    sampleChildOrderInfoPo.setVersion(dto.getVersion());
                    sampleChildOrderInfoPo.setSampleInfoKey(dto.getSampleInfoKey());
                    sampleChildOrderInfoPo.setSampleInfoValue(dto.getSampleInfoValue());
                    sampleChildOrderInfoPo.setSampleChildOrderNo(sampleChildOrderNo);

                    return sampleChildOrderInfoPo;
                }).collect(Collectors.toList());

    }

    public static List<SampleMsgParentVo> poListToMsgVoList(List<SampleChildOrderPo> sampleChildOrderPoList,
                                                            List<SampleParentOrderPo> sampleParentOrderPoList,
                                                            List<SampleParentOrderInfoVo> sampleParentOrderInfoVoList,
                                                            Map<Long, List<String>> fileCodeMap) {
        final Map<String, List<SampleMsgChildVo>> sampleMsgChildMap = sampleChildOrderPoList.stream()
                .map(po -> {
                    final SampleMsgChildVo sampleMsgChildVo = new SampleMsgChildVo();
                    sampleMsgChildVo.setSampleParentOrderNo(po.getSampleParentOrderNo());
                    sampleMsgChildVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleMsgChildVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    return sampleMsgChildVo;
                }).collect(Collectors.groupingBy(SampleMsgChildVo::getSampleParentOrderNo));

        final Map<String, List<SampleParentOrderInfoVo>> infoVoMap = sampleParentOrderInfoVoList.stream()
                .collect(Collectors.groupingBy(SampleParentOrderInfoVo::getSampleParentOrderNo));

        return sampleParentOrderPoList.stream()
                .map(po -> {
                    final SampleMsgParentVo sampleMsgParentVo = new SampleMsgParentVo();
                    sampleMsgParentVo.setSampleParentOrderNo(po.getSampleParentOrderNo());
                    sampleMsgParentVo.setSampleDevType(po.getSampleDevType());
                    sampleMsgParentVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    sampleMsgParentVo.setSampleParentOrderInfoList(infoVoMap.get(po.getSampleParentOrderNo()));
                    sampleMsgParentVo.setSampleMsgChildList(sampleMsgChildMap.get(po.getSampleParentOrderNo()));
                    sampleMsgParentVo.setFileCodeList(fileCodeMap.get(po.getSampleParentOrderId()));
                    return sampleMsgParentVo;
                }).collect(Collectors.toList());

    }

    public static List<SampleInfoVo> childInfoFileCodeToVoList(List<SampleParentOrderPo> sampleParentOrderPoList,
                                                               List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList,
                                                               List<SampleChildOrderPo> sampleChildOrderPoList,
                                                               Map<String, List<String>> skuFileCodeListMap) {
        final Map<String, Long> noIdMap = sampleParentOrderPoList.stream()
                .collect(Collectors.toMap(SampleParentOrderPo::getSampleParentOrderNo, SampleParentOrderPo::getSampleParentOrderId));
        final List<SampleChildOrderInfoVo> sampleChildOrderInfoVoList = childInfoPoListToVoList(sampleChildOrderInfoPoList);

        final Map<String, List<SampleChildOrderInfoVo>> childInfoMap = sampleChildOrderInfoVoList.stream()
                .collect(Collectors.groupingBy(SampleChildOrderInfoVo::getSampleChildOrderNo));

        return sampleChildOrderPoList.stream()
                .map(po -> {
                    final SampleInfoVo sampleInfoVo = new SampleInfoVo();
                    sampleInfoVo.setSku(po.getSku());
                    sampleInfoVo.setSampleChildOrderInfoList(childInfoMap.get(po.getSampleChildOrderNo()));
                    sampleInfoVo.setSupplierCode(po.getSupplierCode());
                    sampleInfoVo.setSupplierName(po.getSupplierName());
                    sampleInfoVo.setDemandDescribe(po.getDemandDescribe());
                    sampleInfoVo.setFileCodeList(skuFileCodeListMap.get(po.getSku()));
                    return sampleInfoVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleNoAndStatusVo> poToStatusVo(List<SampleChildOrderPo> sampleChildOrderPoList,
                                                         List<SampleParentOrderPo> sampleParentOrderPoList) {

        final List<SampleNoAndStatusVo> sampleNoAndStatusVoList1 = Optional.ofNullable(sampleChildOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final SampleNoAndStatusVo sampleNoAndStatusVo = new SampleNoAndStatusVo();
                    sampleNoAndStatusVo.setSampleOrderNo(po.getSampleChildOrderNo());
                    sampleNoAndStatusVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    return sampleNoAndStatusVo;
                }).collect(Collectors.toList());

        final List<SampleNoAndStatusVo> sampleNoAndStatusVoList2 = Optional.ofNullable(sampleParentOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final SampleNoAndStatusVo sampleNoAndStatusVo = new SampleNoAndStatusVo();
                    sampleNoAndStatusVo.setSampleOrderNo(po.getSampleParentOrderNo());
                    sampleNoAndStatusVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    return sampleNoAndStatusVo;
                }).collect(Collectors.toList());

        sampleNoAndStatusVoList1.addAll(sampleNoAndStatusVoList2);

        return sampleNoAndStatusVoList1;
    }

    public static SampleDeliverDetailVo deliverDetailPoToVo(SampleDeliverOrderPo sampleDeliverOrderPo, List<SampleDeliverOrderItemPo> poList) {
        final SampleDeliverDetailVo sampleDeliverDetailVo = new SampleDeliverDetailVo();
        if (null == sampleDeliverOrderPo) {
            return sampleDeliverDetailVo;
        }
        sampleDeliverDetailVo.setSampleDeliverOrderId(sampleDeliverOrderPo.getSampleDeliverOrderId());
        sampleDeliverDetailVo.setVersion(sampleDeliverOrderPo.getVersion());
        sampleDeliverDetailVo.setSampleDeliverOrderNo(sampleDeliverOrderPo.getSampleDeliverOrderNo());
        sampleDeliverDetailVo.setSampleDeliverOrderStatus(sampleDeliverOrderPo.getSampleDeliverOrderStatus());
        sampleDeliverDetailVo.setLogistics(sampleDeliverOrderPo.getLogistics());
        sampleDeliverDetailVo.setTrackingNo(sampleDeliverOrderPo.getTrackingNo());
        sampleDeliverDetailVo.setSupplierCode(sampleDeliverOrderPo.getSupplierCode());
        sampleDeliverDetailVo.setSupplierName(sampleDeliverOrderPo.getSupplierName());
        sampleDeliverDetailVo.setWarehouseCode(sampleDeliverOrderPo.getWarehouseCode());
        sampleDeliverDetailVo.setWarehouseName(sampleDeliverOrderPo.getWarehouseName());
        sampleDeliverDetailVo.setWarehouseTypeList(FormatStringUtil.string2List(sampleDeliverOrderPo.getWarehouseTypes(), ","));
        sampleDeliverDetailVo.setDeliverUsername(sampleDeliverOrderPo.getDeliverUsername());
        sampleDeliverDetailVo.setDeliverTime(sampleDeliverOrderPo.getDeliverTime());

        if (CollectionUtils.isEmpty(poList)) {
            return sampleDeliverDetailVo;
        }
        final List<SampleDeliverItemVo> sampleDeliverItemVoList = poList.stream()
                .map(po -> {
                    final SampleDeliverItemVo SampleDeliverItemVo = new SampleDeliverItemVo();
                    SampleDeliverItemVo.setSpu(po.getSpu());
                    SampleDeliverItemVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    SampleDeliverItemVo.setDeliverCnt(po.getDeliverCnt());

                    return SampleDeliverItemVo;
                }).collect(Collectors.toList());
        sampleDeliverDetailVo.setSampleDeliverItemList(sampleDeliverItemVoList);

        return sampleDeliverDetailVo;
    }

    public static List<SampleChildOrderProcessPo> parentProcessToChildProcess(List<SampleParentOrderProcessPo> sampleParentOrderProcessPoList,
                                                                              List<SampleChildOrderPo> SampleChildOrderPoList) {
        if (CollectionUtils.isEmpty(sampleParentOrderProcessPoList)) {
            return Collections.emptyList();
        }

        return sampleParentOrderProcessPoList.stream()
                .map(parentProcessPo -> SampleChildOrderPoList.stream().map(
                        childPo -> {
                            final SampleChildOrderProcessPo sampleChildOrderProcessPo = new SampleChildOrderProcessPo();
                            sampleChildOrderProcessPo.setSampleChildOrderId(childPo.getSampleChildOrderId());
                            sampleChildOrderProcessPo.setSampleParentOrderNo(childPo.getSampleParentOrderNo());
                            sampleChildOrderProcessPo.setSampleChildOrderNo(childPo.getSampleChildOrderNo());
                            sampleChildOrderProcessPo.setProcessCode(parentProcessPo.getProcessCode());
                            sampleChildOrderProcessPo.setProcessName(parentProcessPo.getProcessName());
                            sampleChildOrderProcessPo.setProcessSecondCode(parentProcessPo.getProcessSecondCode());
                            sampleChildOrderProcessPo.setProcessSecondName(parentProcessPo.getProcessSecondName());
                            sampleChildOrderProcessPo.setProcessLabel(parentProcessPo.getProcessLabel());

                            return sampleChildOrderProcessPo;
                        }
                ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<SampleChildOrderProcessDescPo> parentProcessDescToChildProcessDesc(List<SampleParentOrderProcessDescPo> sampleParentOrderProcessDescPoList,
                                                                                          List<SampleChildOrderPo> SampleChildOrderPoList) {
        return sampleParentOrderProcessDescPoList.stream()
                .map(parentPo -> SampleChildOrderPoList.stream().map(
                        childPo -> {
                            final SampleChildOrderProcessDescPo sampleChildOrderProcessDescPo = new SampleChildOrderProcessDescPo();
                            sampleChildOrderProcessDescPo.setSampleChildOrderId(childPo.getSampleChildOrderId());
                            sampleChildOrderProcessDescPo.setSampleParentOrderNo(childPo.getSampleParentOrderNo());
                            sampleChildOrderProcessDescPo.setSampleChildOrderNo(childPo.getSampleChildOrderNo());
                            sampleChildOrderProcessDescPo.setName(parentPo.getName());
                            sampleChildOrderProcessDescPo.setDescValue(parentPo.getDescValue());


                            return sampleChildOrderProcessDescPo;
                        }
                ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<SampleChildOrderRawPo> parentRawToChildRaw(List<SampleParentOrderRawPo> sampleParentOrderRawPoList,
                                                                  List<SampleChildOrderPo> sampleChildOrderPoList,
                                                                  SampleRawBizType sampleRawBizType) {
        return sampleParentOrderRawPoList.stream()
                .map(parentPo -> sampleChildOrderPoList.stream().map(
                        childPo -> {
                            final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                            sampleChildOrderRawPo.setSampleChildOrderId(childPo.getSampleChildOrderId());
                            sampleChildOrderRawPo.setSampleParentOrderNo(childPo.getSampleParentOrderNo());
                            sampleChildOrderRawPo.setSampleChildOrderNo(childPo.getSampleChildOrderNo());
                            sampleChildOrderRawPo.setSku(parentPo.getSku());
                            sampleChildOrderRawPo.setDeliveryCnt(parentPo.getDeliveryCnt());
                            sampleChildOrderRawPo.setSampleRawBizType(sampleRawBizType);


                            return sampleChildOrderRawPo;
                        }
                ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<SampleProcessVo> childProcessPoToVo(List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList) {
        return Optional.ofNullable(sampleChildOrderProcessPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(childProcessPo -> {
                    final SampleProcessVo sampleProcessVo = new SampleProcessVo();
                    sampleProcessVo.setSampleChildOrderProcessId(childProcessPo.getSampleChildOrderProcessId());
                    sampleProcessVo.setVersion(childProcessPo.getVersion());
                    sampleProcessVo.setProcessCode(childProcessPo.getProcessCode());
                    sampleProcessVo.setProcessName(childProcessPo.getProcessName());
                    sampleProcessVo.setProcessSecondCode(childProcessPo.getProcessSecondCode());
                    sampleProcessVo.setProcessSecondName(childProcessPo.getProcessSecondName());
                    sampleProcessVo.setProcessLabel(childProcessPo.getProcessLabel());
                    sampleProcessVo.setSampleChildOrderNo(childProcessPo.getSampleChildOrderNo());

                    return sampleProcessVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleRawVo> childRawPoToVo(List<SampleChildOrderRawPo> sampleChildOrderRawPoList, Map<String, String> skuEncodeMap) {
        return Optional.ofNullable(sampleChildOrderRawPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(childRawPo -> {
                    final SampleRawVo sampleRawVo = new SampleRawVo();
                    sampleRawVo.setSampleOrderRawId(childRawPo.getSampleChildOrderRawId());
                    sampleRawVo.setVersion(childRawPo.getVersion());
                    sampleRawVo.setSku(childRawPo.getSku());
                    sampleRawVo.setDeliveryCnt(childRawPo.getDeliveryCnt());
                    sampleRawVo.setSkuEncode(skuEncodeMap.get(childRawPo.getSku()));
                    sampleRawVo.setSampleChildOrderNo(childRawPo.getSampleChildOrderNo());

                    return sampleRawVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderProcessPo> processDtoToPo(SampleChildOrderPo sampleChildOrderPo, List<SampleProcessDto> sampleProcessList) {
        return Optional.ofNullable(sampleProcessList)
                .orElse(Collections.emptyList())
                .stream()
                .map(processDto -> {
                    final SampleChildOrderProcessPo sampleChildOrderProcessPo = new SampleChildOrderProcessPo();
                    sampleChildOrderProcessPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderProcessPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderProcessPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderProcessPo.setProcessCode(processDto.getProcessCode());
                    sampleChildOrderProcessPo.setProcessName(processDto.getProcessName());
                    sampleChildOrderProcessPo.setProcessSecondCode(processDto.getProcessSecondCode());
                    sampleChildOrderProcessPo.setProcessSecondName(processDto.getProcessSecondName());
                    sampleChildOrderProcessPo.setProcessLabel(processDto.getProcessLabel());
                    return sampleChildOrderProcessPo;
                }).collect(Collectors.toList());

    }

    public static List<SampleChildOrderRawPo> rawDtoToPo(SampleChildOrderPo sampleChildOrderPo, List<SampleRawDto> sampleRawList) {
        return Optional.ofNullable(sampleRawList)
                .orElse(Collections.emptyList())
                .stream()
                .map(rawDto -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                    sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderRawPo.setSku(rawDto.getSku());
                    sampleChildOrderRawPo.setDeliveryCnt(rawDto.getDeliveryCnt());
                    sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.FORMULA);

                    return sampleChildOrderRawPo;
                }).collect(Collectors.toList());
    }

    public static List<SampleProcessDescVo> childProcessDescPoToVo(List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList) {
        return Optional.ofNullable(sampleChildOrderProcessDescPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(processPo -> {
                    final SampleProcessDescVo sampleProcessDescVo = new SampleProcessDescVo();
                    sampleProcessDescVo.setSampleChildOrderProcessDescId(processPo.getSampleChildOrderProcessDescId());
                    sampleProcessDescVo.setVersion(processPo.getVersion());
                    sampleProcessDescVo.setName(processPo.getName());
                    sampleProcessDescVo.setDescValue(processPo.getDescValue());
                    sampleProcessDescVo.setSampleChildOrderNo(processPo.getSampleChildOrderNo());

                    return sampleProcessDescVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderPo> sampleChildItemDtoToChildOrderList(List<SampleChildItemDto> sampleChildItemList,
                                                                              SampleParentOrderPo sampleParentOrderPo,
                                                                              SampleOrderStatus targetStatus,
                                                                              Map<String, SampleProduceLabel> sampleProduceLabelMap, Map<String, String> supplierNameMap) {
        List<SampleChildOrderPo> sampleChildOrderList = new ArrayList<>();

        int childOrderNoIndex = 0;
        for (SampleChildItemDto item : sampleChildItemList) {
            SampleChildOrderPo sampleChildOrderPo = new SampleChildOrderPo();
            final String indexStr = StringUtil.toTwoDigitFormat(++childOrderNoIndex);
            sampleChildOrderPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
            sampleChildOrderPo.setSampleChildOrderNo(sampleParentOrderPo.getSampleParentOrderNo() + "-" + indexStr);
            sampleChildOrderPo.setSampleOrderStatus(targetStatus);
            sampleChildOrderPo.setSpu(sampleParentOrderPo.getSpu());
            sampleChildOrderPo.setSupplierCode(item.getSupplierCode());
            sampleChildOrderPo.setSupplierName(supplierNameMap.get(item.getSupplierCode()));
            sampleChildOrderPo.setPurchaseCnt(item.getPurchaseCnt());
            sampleChildOrderPo.setWarehouseName(sampleParentOrderPo.getWarehouseName());
            sampleChildOrderPo.setWarehouseCode(sampleParentOrderPo.getWarehouseCode());
            sampleChildOrderPo.setWarehouseTypes(sampleParentOrderPo.getWarehouseTypes());
            sampleChildOrderPo.setDeliverDate(sampleParentOrderPo.getDeliverDate());
            sampleChildOrderPo.setPlatform(sampleParentOrderPo.getPlatform());
            sampleChildOrderPo.setSampleDescribe(sampleParentOrderPo.getSampleDescribe());
            sampleChildOrderPo.setSupplierProduction(item.getSupplierProduction());
            sampleChildOrderPo.setProofingPrice(item.getProofingPrice());
            sampleChildOrderPo.setCostPrice(item.getCostPrice());
            sampleChildOrderPo.setReceiptCnt(item.getPurchaseCnt());
            sampleChildOrderPo.setSampleDevType(sampleParentOrderPo.getSampleDevType());
            sampleChildOrderPo.setSku(item.getSku());
            sampleChildOrderPo.setRawWarehouseCode(sampleParentOrderPo.getRawWarehouseCode());
            sampleChildOrderPo.setRawWarehouseName(sampleParentOrderPo.getRawWarehouseName());

            sampleChildOrderPo.setSampleProduceLabel(sampleProduceLabelMap.get(item.getSku()));

            sampleChildOrderList.add(sampleChildOrderPo);
        }

        return sampleChildOrderList;
    }

    public static List<RawSampleItemVo> rawPoListToVoList(List<SampleChildOrderRawPo> demandSampleChildOrderRawPoList) {
        return Optional.ofNullable(demandSampleChildOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final RawSampleItemVo rawSampleItemVo = new RawSampleItemVo();
                    rawSampleItemVo.setSampleChildOrderRawId(po.getSampleChildOrderRawId());
                    rawSampleItemVo.setVersion(po.getVersion());
                    rawSampleItemVo.setSku(po.getSku());
                    rawSampleItemVo.setSkuBatchCode(po.getSkuBatchCode());
                    rawSampleItemVo.setDeliveryCnt(po.getDeliveryCnt());
                    return rawSampleItemVo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderRawPo> sampleChildTypesetToChildRaw(List<SampleRawDto> sampleRawList,
                                                                           SampleChildOrderPo sampleChildOrderPo,
                                                                           SampleRawBizType sampleRawBizType) {
        return sampleRawList.stream()
                .map(sampleRaw -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                    sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderRawPo.setSku(sampleRaw.getSku());
                    sampleChildOrderRawPo.setDeliveryCnt(sampleRaw.getDeliveryCnt());
                    sampleChildOrderRawPo.setSampleRawBizType(sampleRawBizType);
                    return sampleChildOrderRawPo;
                }).collect(Collectors.toList());

    }

    public static List<SampleChildOrderRawPo> sampleChildTypesetToMultiplyChildRaw(List<SampleRawDto> sampleRawList,
                                                                                   SampleChildOrderPo sampleChildOrderPo,
                                                                                   SampleRawBizType sampleRawBizType) {
        return sampleRawList.stream()
                .map(sampleRaw -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                    sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderRawPo.setSku(sampleRaw.getSku());
                    sampleChildOrderRawPo.setDeliveryCnt(sampleRaw.getDeliveryCnt() * sampleChildOrderPo.getPurchaseCnt());
                    sampleChildOrderRawPo.setSampleRawBizType(sampleRawBizType);
                    return sampleChildOrderRawPo;
                }).collect(Collectors.toList());

    }


    public static List<SampleChildOrderRawPo> editRawDtoToPo(SampleChildOrderPo sampleChildOrderPo, List<SampleRawDto> sampleRawList) {
        return Optional.ofNullable(sampleRawList)
                .orElse(Collections.emptyList())
                .stream()
                .map(rawDto -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                    sampleChildOrderRawPo.setSampleChildOrderRawId(rawDto.getSampleChildOrderRawId());
                    sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.FORMULA);
                    sampleChildOrderRawPo.setVersion(rawDto.getVersion());
                    sampleChildOrderRawPo.setSku(rawDto.getSku());
                    sampleChildOrderRawPo.setDeliveryCnt(rawDto.getDeliveryCnt());
                    return sampleChildOrderRawPo;
                }).collect(Collectors.toList());
    }

    public static List<SampleChildOrderProcessPo> editProcessDtoToPo(SampleChildOrderPo sampleChildOrderPo, List<SampleProcessDto> sampleProcessList) {
        return Optional.ofNullable(sampleProcessList)
                .orElse(Collections.emptyList())
                .stream()
                .map(processDto -> {
                    final SampleChildOrderProcessPo sampleChildOrderProcessPo = new SampleChildOrderProcessPo();
                    sampleChildOrderProcessPo.setSampleChildOrderProcessId(processDto.getSampleChildOrderProcessId());
                    sampleChildOrderProcessPo.setVersion(processDto.getVersion());
                    sampleChildOrderProcessPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderProcessPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderProcessPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderProcessPo.setProcessCode(processDto.getProcessCode());
                    sampleChildOrderProcessPo.setProcessName(processDto.getProcessName());
                    sampleChildOrderProcessPo.setProcessSecondCode(processDto.getProcessSecondCode());
                    sampleChildOrderProcessPo.setProcessSecondName(processDto.getProcessSecondName());
                    sampleChildOrderProcessPo.setProcessCode(processDto.getProcessCode());
                    sampleChildOrderProcessPo.setProcessName(processDto.getProcessName());
                    sampleChildOrderProcessPo.setProcessLabel(processDto.getProcessLabel());
                    return sampleChildOrderProcessPo;
                }).collect(Collectors.toList());

    }

    public static List<SampleChildOrderProcessDescPo> editProcessDescDtoToPo(SampleChildOrderPo sampleChildOrderPo, List<SampleProcessDescDto> sampleProcessDescList) {
        return Optional.ofNullable(sampleProcessDescList)
                .orElse(Collections.emptyList())
                .stream()
                .map(processDescDto -> {
                    final SampleChildOrderProcessDescPo sampleChildOrderProcessDescPo = new SampleChildOrderProcessDescPo();
                    sampleChildOrderProcessDescPo.setSampleChildOrderProcessDescId(processDescDto.getSampleChildOrderProcessDescId());
                    sampleChildOrderProcessDescPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                    sampleChildOrderProcessDescPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleChildOrderProcessDescPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleChildOrderProcessDescPo.setVersion(processDescDto.getVersion());
                    sampleChildOrderProcessDescPo.setName(processDescDto.getName());
                    sampleChildOrderProcessDescPo.setDescValue(processDescDto.getDescValue());
                    return sampleChildOrderProcessDescPo;
                }).collect(Collectors.toList());

    }

    public static List<SampleParentProcessVo> parentProcessPoToVo(List<SampleParentOrderProcessPo> sampleParentOrderProcessPoList) {
        return Optional.ofNullable(sampleParentOrderProcessPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(parentProcessPo -> {
                    final SampleParentProcessVo sampleParentProcessVo = new SampleParentProcessVo();
                    sampleParentProcessVo.setProcessCode(parentProcessPo.getProcessCode());
                    sampleParentProcessVo.setProcessName(parentProcessPo.getProcessName());
                    sampleParentProcessVo.setProcessSecondCode(parentProcessPo.getProcessSecondCode());
                    sampleParentProcessVo.setProcessSecondName(parentProcessPo.getProcessSecondName());
                    sampleParentProcessVo.setProcessLabel(parentProcessPo.getProcessLabel());
                    return sampleParentProcessVo;
                }).collect(Collectors.toList());
    }

    /**
     * 通过样品工序转换加工工序信息
     *
     * @param sampleChildOrderProcessPoList:
     * @param processCodeMap:
     * @return List<SampleProcessVo>
     * @author ChenWenLong
     * @date 2023/6/14 14:25
     */
    public static List<SampleProcessVo> childSampleProcessPoToProcessVo(List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList,
                                                                        Map<String, Long> processCodeMap) {
        return Optional.ofNullable(sampleChildOrderProcessPoList)
                .orElse(new ArrayList<>())
                .stream()
                .map(childProcessPo -> {
                    final SampleProcessVo sampleProcessVo = new SampleProcessVo();
                    sampleProcessVo.setSampleChildOrderProcessId(childProcessPo.getSampleChildOrderProcessId());
                    sampleProcessVo.setVersion(childProcessPo.getVersion());
                    sampleProcessVo.setProcessCode(childProcessPo.getProcessCode());
                    sampleProcessVo.setProcessName(childProcessPo.getProcessName());
                    sampleProcessVo.setProcessSecondCode(childProcessPo.getProcessSecondCode());
                    sampleProcessVo.setProcessSecondName(childProcessPo.getProcessSecondName());
                    sampleProcessVo.setProcessLabel(childProcessPo.getProcessLabel());
                    sampleProcessVo.setSampleChildOrderNo(childProcessPo.getSampleChildOrderNo());
                    sampleProcessVo.setProcessId(processCodeMap.get(childProcessPo.getProcessCode()));

                    return sampleProcessVo;
                }).collect(Collectors.toList());
    }
}
