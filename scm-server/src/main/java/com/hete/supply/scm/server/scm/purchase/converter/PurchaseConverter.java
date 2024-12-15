package com.hete.supply.scm.server.scm.purchase.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseChildPreConfirmExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverOrderBo;
import com.hete.supply.scm.server.scm.entity.bo.RawReceiveOrderBo;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.entity.dto.RawProductItemDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataPo;
import com.hete.supply.scm.server.scm.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildAndItemBo;
import com.hete.supply.scm.server.scm.purchase.entity.dto.*;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.purchase.entity.vo.*;
import com.hete.supply.scm.server.scm.purchase.enums.RawExtra;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierInventoryPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.ConfirmCommissioningMsgVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDeliverDetailVo;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.PurchaseDemandRawVo;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.TimeZoneId;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/4 13:51
 */
public class PurchaseConverter {
    /**
     * createDto 转 purchaseParentPo
     *
     * @param dto
     * @return
     */
    public static PurchaseParentOrderPo createDtoToPo(PurchaseCreateDto dto) {

        PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
        purchaseParentOrderPo.setSpu(dto.getSpu());
        purchaseParentOrderPo.setPlatform(dto.getPlatform());
        purchaseParentOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseParentOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.WAIT_APPROVE);
        final List<PurchaseProductDemandItemDto> purchaseProductDemandItemList = dto.getPurchaseProductDemandItemList();
        final List<String> skuList = purchaseProductDemandItemList.stream()
                .map(PurchaseProductDemandItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        purchaseParentOrderPo.setSkuCnt(skuList.size());
        final int purchaseTotal = purchaseProductDemandItemList.stream()
                .mapToInt(PurchaseProductDemandItemDto::getPurchaseCnt)
                .sum();
        purchaseParentOrderPo.setPurchaseTotal(purchaseTotal);
        purchaseParentOrderPo.setOrderRemarks(dto.getOrderRemarks());
        purchaseParentOrderPo.setSkuType(dto.getSkuType());
        purchaseParentOrderPo.setPurchaseDemandType(dto.getPurchaseDemandType());
        purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());

        return purchaseParentOrderPo;
    }


    public static PurchaseParentOrderPo itemDtoToRawPurchaseParentPo(PurchaseParentOrderPo sourcePurchaseParentOrderPo,
                                                                     String spu,
                                                                     SupplierWarehousePo supplierWarehousePo,
                                                                     Integer purchaseTotal) {
        PurchaseParentOrderPo purchaseParentOrderPo = new PurchaseParentOrderPo();
        purchaseParentOrderPo.setSpu(spu);
        purchaseParentOrderPo.setPlatform(sourcePurchaseParentOrderPo.getPlatform());
        purchaseParentOrderPo.setWarehouseName(supplierWarehousePo.getWarehouseName());
        purchaseParentOrderPo.setWarehouseCode(supplierWarehousePo.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseTypes(supplierWarehousePo.getSupplierWarehouse().getRemark());
        purchaseParentOrderPo.setPurchaseParentOrderStatus(PurchaseParentOrderStatus.WAIT_APPROVE);
        purchaseParentOrderPo.setSkuCnt(1);
        purchaseParentOrderPo.setPurchaseTotal(purchaseTotal);
        purchaseParentOrderPo.setSkuType(SkuType.SKU);
        purchaseParentOrderPo.setPurchaseDemandType(PurchaseDemandType.NORMAL);
        purchaseParentOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
        purchaseParentOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());

        return purchaseParentOrderPo;
    }


    public static PurchaseDetailNewVo poToDetailNewVo(PurchaseParentOrderPo purchaseParentOrderPo,
                                                      List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList,
                                                      List<PurchaseChildOrderPo> purchaseChildOrderPoList,
                                                      Map<String, String> skuEncodeMap,
                                                      Map<String, PurchaseChildOrderItemPo> purchaseChildNoItemMap,
                                                      Map<String, Integer> skuPlacedCntMap,
                                                      Map<String, Integer> skuDeliveredCntMap,
                                                      Map<String, Integer> returnCntMap,
                                                      Map<String, Integer> childOrderDeliveredCntMap,
                                                      Map<String, Integer> purchaseNoReturnCntMap,
                                                      Map<String, List<String>> skuImageMap,
                                                      Map<String, Integer> skuInTransitCntMap,
                                                      Map<String, Integer> skuWaitDeliverMap) {
        final PurchaseDetailNewVo purchaseDetailNewVo = new PurchaseDetailNewVo();
        purchaseDetailNewVo.setPurchaseParentOrderId(purchaseParentOrderPo.getPurchaseParentOrderId());
        purchaseDetailNewVo.setVersion(purchaseParentOrderPo.getVersion());
        purchaseDetailNewVo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        purchaseDetailNewVo.setSkuType(purchaseParentOrderPo.getSkuType());
        purchaseDetailNewVo.setPlatform(purchaseParentOrderPo.getPlatform());
        purchaseDetailNewVo.setWarehouseName(purchaseParentOrderPo.getWarehouseName());
        purchaseDetailNewVo.setWarehouseCode(purchaseParentOrderPo.getWarehouseCode());
        purchaseDetailNewVo.setWarehouseTypeList(FormatStringUtil.string2List(purchaseParentOrderPo.getWarehouseTypes(), ","));
        purchaseDetailNewVo.setOrderRemarks(purchaseParentOrderPo.getOrderRemarks());
        purchaseDetailNewVo.setPurchaseParentOrderStatus(purchaseParentOrderPo.getPurchaseParentOrderStatus());
        purchaseDetailNewVo.setSkuCnt(purchaseParentOrderItemPoList.size());
        purchaseDetailNewVo.setPurchaseDemandType(purchaseParentOrderPo.getPurchaseDemandType());
        purchaseDetailNewVo.setDevelopChildOrderNo(purchaseParentOrderPo.getDevelopChildOrderNo());

        final List<PurchaseDetailNewItemVo> purchaseDetailNewItemList = purchaseParentOrderItemPoList.stream()
                .map(itemPo -> {
                    final PurchaseDetailNewItemVo purchaseDetailNewItemVo = new PurchaseDetailNewItemVo();
                    purchaseDetailNewItemVo.setSku(itemPo.getSku());
                    purchaseDetailNewItemVo.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                    purchaseDetailNewItemVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                    purchaseDetailNewItemVo.setQualityGoodsCnt(itemPo.getQualityGoodsCnt());
                    purchaseDetailNewItemVo.setDefectiveGoodsCnt(itemPo.getDefectiveGoodsCnt());
                    purchaseDetailNewItemVo.setWarehousedCnt(itemPo.getQualityGoodsCnt());
                    final Integer placedCnt = skuPlacedCntMap.getOrDefault(itemPo.getSku(), 0);
                    purchaseDetailNewItemVo.setPlacedCnt(placedCnt);
                    purchaseDetailNewItemVo.setCanSplitCnt(itemPo.getCanSplitCnt());
                    final Integer deliveredCnt = skuDeliveredCntMap.getOrDefault(itemPo.getSku(), 0);
                    purchaseDetailNewItemVo.setWaitDeliverCnt(skuWaitDeliverMap.getOrDefault(itemPo.getSku(), 0));
                    purchaseDetailNewItemVo.setDeliveredCnt(deliveredCnt);
                    purchaseDetailNewItemVo.setReturnCnt(returnCntMap.getOrDefault(itemPo.getSku(), 0));
                    purchaseDetailNewItemVo.setInTransitCnt(skuInTransitCntMap.getOrDefault(itemPo.getSku(), 0));

                    return purchaseDetailNewItemVo;
                }).collect(Collectors.toList());

        purchaseDetailNewVo.setPurchaseDetailNewItemList(purchaseDetailNewItemList);

        final List<PurchaseChildDetailItemVo> purchaseChildDetailItemList = Optional.ofNullable(purchaseChildOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(childPo -> {
                    final PurchaseChildDetailItemVo purchaseChildDetailItemVo = new PurchaseChildDetailItemVo();
                    purchaseChildDetailItemVo.setPurchaseChildOrderId(childPo.getPurchaseChildOrderId());
                    purchaseChildDetailItemVo.setVersion(childPo.getVersion());
                    purchaseChildDetailItemVo.setPurchaseChildOrderNo(childPo.getPurchaseChildOrderNo());
                    purchaseChildDetailItemVo.setPurchaseBizType(childPo.getPurchaseBizType());
                    purchaseChildDetailItemVo.setExpectedOnShelvesDate(childPo.getExpectedOnShelvesDate());
                    final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildNoItemMap.get(childPo.getPurchaseChildOrderNo());
                    purchaseChildDetailItemVo.setSku(purchaseChildOrderItemPo.getSku());
                    purchaseChildDetailItemVo.setSkuEncode(skuEncodeMap.get(purchaseChildOrderItemPo.getSku()));
                    purchaseChildDetailItemVo.setPlacedCnt(purchaseChildOrderItemPo.getPurchaseCnt());
                    purchaseChildDetailItemVo.setQualityGoodsCnt(purchaseChildOrderItemPo.getQualityGoodsCnt());
                    purchaseChildDetailItemVo.setDefectiveGoodsCnt(purchaseChildOrderItemPo.getDefectiveGoodsCnt());
                    final Integer deliveredCnt = childOrderDeliveredCntMap.getOrDefault(childPo.getPurchaseChildOrderNo(), 0);
                    purchaseChildDetailItemVo.setDeliveredCnt(deliveredCnt);
                    purchaseChildDetailItemVo.setWarehousedCnt(purchaseChildOrderItemPo.getQualityGoodsCnt());
                    purchaseChildDetailItemVo.setReturnCnt(purchaseNoReturnCntMap.getOrDefault(childPo.getPurchaseChildOrderNo(), 0));
                    purchaseChildDetailItemVo.setPurchaseCnt(purchaseChildOrderItemPo.getPurchaseCnt());
                    purchaseChildDetailItemVo.setPurchaseOrderStatus(childPo.getPurchaseOrderStatus());
                    purchaseChildDetailItemVo.setFileCodeList(skuImageMap.get(purchaseChildOrderItemPo.getSku()));

                    return purchaseChildDetailItemVo;
                }).collect(Collectors.toList());

        purchaseDetailNewVo.setPurchaseChildDetailItemList(purchaseChildDetailItemList);

        return purchaseDetailNewVo;

    }

    public static PurchaseChildAndItemBo splitDtoItemToPurchaseChildPo(List<PurchaseSplitItemDto> purchaseSplitItemList,
                                                                       PurchaseParentOrderPo purchaseParentOrderPo,
                                                                       int skuSize,
                                                                       Map<String, List<PurchaseChildOrderItemPo>> skuChildItemMap) {
        List<PurchaseChildOrderPo> purchaseChildOrderPoList = new ArrayList<>();
        List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = new ArrayList<>();

        List<String> historySkuList = new ArrayList<>();
        purchaseSplitItemList.stream()
                .sorted(Comparator.comparing(PurchaseSplitItemDto::getExpectedOnShelvesDate))
                .forEach(itemDto -> {
                    PurchaseChildOrderPo purchaseChildOrderPo = new PurchaseChildOrderPo();
                    purchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
                    purchaseChildOrderPo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
                    purchaseChildOrderPo.setPurchaseChildOrderNo(itemDto.getPurchaseChildOrderNo());
                    purchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_CONFIRM);
                    if (PurchaseDemandType.WH.equals(purchaseParentOrderPo.getPurchaseDemandType())) {
                        purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.WH);
                    } else if (!historySkuList.contains(itemDto.getSku()) && skuChildItemMap.get(itemDto.getSku()) == null) {
                        purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.FIRST_ORDER);
                    } else if (PurchaseDemandType.SPECIAL.equals(purchaseParentOrderPo.getPurchaseDemandType())) {
                        purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.SPECIAL);
                    } else {
                        purchaseChildOrderPo.setPurchaseOrderType(PurchaseOrderType.NORMAL);
                    }
                    if (StringUtils.isNotBlank(purchaseParentOrderPo.getDevelopChildOrderNo())) {
                        purchaseChildOrderPo.setPurchaseOrderType(itemDto.getPurchaseOrderType());
                    }
                    historySkuList.add(itemDto.getSku());
                    purchaseChildOrderPo.setIsUrgentOrder(BooleanType.FALSE);
                    purchaseChildOrderPo.setIsNormalOrder(BooleanType.FALSE);
                    purchaseChildOrderPo.setSpu(purchaseParentOrderPo.getSpu());
                    purchaseChildOrderPo.setPlatform(purchaseParentOrderPo.getPlatform());
                    purchaseChildOrderPo.setPurchaseBizType(PurchaseBizType.NO_TYPE);
                    purchaseChildOrderPo.setOrderRemarks(itemDto.getOrderRemarks());
                    purchaseChildOrderPo.setExpectedOnShelvesDate(itemDto.getExpectedOnShelvesDate());
                    purchaseChildOrderPo.setIsUploadOverseasMsg(BooleanType.FALSE);
                    purchaseChildOrderPo.setSkuCnt(skuSize);
                    purchaseChildOrderPo.setPurchaseTotal(itemDto.getPurchaseCnt());
                    purchaseChildOrderPo.setRawRemainTab(BooleanType.TRUE);
                    purchaseChildOrderPo.setWarehouseCode(itemDto.getWarehouseCode());
                    purchaseChildOrderPo.setWarehouseName(itemDto.getWarehouseName());
                    purchaseChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(itemDto.getWarehouseTypeList())
                            .orElse(new ArrayList<>())));
                    purchaseChildOrderPo.setIsOverdue(BooleanType.FALSE);
                    purchaseChildOrderPo.setShippableCnt(itemDto.getPurchaseCnt());
                    purchaseChildOrderPo.setSkuType(purchaseParentOrderPo.getSkuType());
                    purchaseChildOrderPo.setSplitType(SplitType.GOODS_SPLIT);
                    purchaseChildOrderPo.setPromiseDate(itemDto.getExpectedOnShelvesDate());
                    purchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
                    purchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
                    purchaseChildOrderPo.setOrderSource(OrderSource.SCM);
                    purchaseChildOrderPoList.add(purchaseChildOrderPo);

                    final PurchaseChildOrderItemPo purchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
                    purchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    purchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                    purchaseChildOrderItemPo.setSku(itemDto.getSku());
                    purchaseChildOrderItemPo.setPurchaseCnt(itemDto.getPurchaseCnt());
                    purchaseChildOrderItemPo.setInitPurchaseCnt(itemDto.getPurchaseCnt());
                    purchaseChildOrderItemPo.setUndeliveredCnt(itemDto.getPurchaseCnt());
                    purchaseChildOrderItemPoList.add(purchaseChildOrderItemPo);
                });

        return PurchaseChildAndItemBo.builder()
                .purchaseChildOrderPoList(purchaseChildOrderPoList)
                .purchaseChildOrderItemPoList(purchaseChildOrderItemPoList)
                .build();
    }

    public static List<PurchaseChildOrderRawPo> splitDtoToRawItemPo(List<RawProductItemDto> rawProductItemList,
                                                                    PurchaseChildOrderPo purchaseChildOrderPo,
                                                                    PurchaseRawBizType purchaseRawBizType) {
        return rawProductItemList.stream()
                .map(rawItem -> {
                    final PurchaseChildOrderRawPo purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
                    purchaseChildOrderRawPo.setPurchaseChildOrderRawId(rawItem.getPurchaseChildOrderRawId());
                    purchaseChildOrderRawPo.setVersion(rawItem.getVersion());
                    purchaseChildOrderRawPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                    purchaseChildOrderRawPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    purchaseChildOrderRawPo.setSku(rawItem.getSku());
                    purchaseChildOrderRawPo.setDeliveryCnt(rawItem.getDeliveryCnt());
                    purchaseChildOrderRawPo.setPurchaseRawBizType(purchaseRawBizType);
                    purchaseChildOrderRawPo.setRawSupplier(rawItem.getRawSupplier());
                    purchaseChildOrderRawPo.setRawExtra(RawExtra.NORMAL);
                    purchaseChildOrderRawPo.setDispenseCnt(rawItem.getDeliveryCnt());
                    purchaseChildOrderRawPo.setRawWarehouseCode(rawItem.getRawWarehouseCode());
                    purchaseChildOrderRawPo.setRawWarehouseName(rawItem.getRawWarehouseName());

                    return purchaseChildOrderRawPo;
                }).collect(Collectors.toList());
    }

    public static List<PurchaseChildOrderRawPo> splitDtoToRawItemBomPo(List<RawProductItemDto> rawProductItemList,
                                                                       PurchaseChildOrderPo purchaseChildOrderPo,
                                                                       PurchaseRawBizType purchaseRawBizType) {
        return rawProductItemList.stream()
                .map(rawItem -> {
                    final PurchaseChildOrderRawPo purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
                    purchaseChildOrderRawPo.setPurchaseChildOrderRawId(rawItem.getPurchaseChildOrderRawId());
                    purchaseChildOrderRawPo.setVersion(rawItem.getVersion());
                    purchaseChildOrderRawPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                    purchaseChildOrderRawPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                    purchaseChildOrderRawPo.setSku(rawItem.getSku());
                    purchaseChildOrderRawPo.setDeliveryCnt(rawItem.getBomCnt());
                    purchaseChildOrderRawPo.setPurchaseRawBizType(purchaseRawBizType);
                    purchaseChildOrderRawPo.setRawSupplier(rawItem.getRawSupplier());
                    purchaseChildOrderRawPo.setRawExtra(RawExtra.NORMAL);

                    return purchaseChildOrderRawPo;
                }).collect(Collectors.toList());
    }

    public static void editDtoConvertPo(PurchaseEditDto dto, PurchaseParentOrderPo purchaseParentOrderPo) {
        purchaseParentOrderPo.setSpu(dto.getSpu());
        purchaseParentOrderPo.setPlatform(dto.getPlatform());
        purchaseParentOrderPo.setWarehouseName(dto.getWarehouseName());
        purchaseParentOrderPo.setWarehouseCode(dto.getWarehouseCode());
        purchaseParentOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        purchaseParentOrderPo.setOrderRemarks(dto.getOrderRemarks());
        final List<PurchaseProductDemandItemDto> purchaseProductDemandItemList = dto.getPurchaseProductDemandItemList();
        final List<String> skuList = purchaseProductDemandItemList.stream()
                .map(PurchaseProductDemandItemDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        final int purchaseTotal = purchaseProductDemandItemList.stream()
                .mapToInt(PurchaseProductDemandItemDto::getPurchaseCnt)
                .sum();
        purchaseParentOrderPo.setSkuCnt(skuList.size());
        purchaseParentOrderPo.setPurchaseTotal(purchaseTotal);
        purchaseParentOrderPo.setCanSplitCnt(purchaseTotal);
        purchaseParentOrderPo.setSkuType(dto.getSkuType());
    }

    public static PurchaseParentOrderChangePo parentPoToChangePo(PurchaseParentOrderPo purchaseParentOrderPo) {
        PurchaseParentOrderChangePo purchaseParentOrderChangePo = new PurchaseParentOrderChangePo();
        purchaseParentOrderChangePo.setPurchaseParentOrderId(purchaseParentOrderPo.getPurchaseParentOrderId());
        purchaseParentOrderChangePo.setPurchaseParentOrderNo(purchaseParentOrderPo.getPurchaseParentOrderNo());
        purchaseParentOrderChangePo.setPlaceOrderTime(new DateTime().toLocalDateTime());
        purchaseParentOrderChangePo.setPlaceOrderUser(purchaseParentOrderPo.getPlaceOrderUser());
        purchaseParentOrderChangePo.setPlaceOrderUsername(purchaseParentOrderPo.getPlaceOrderUsername());
        return purchaseParentOrderChangePo;
    }

    public static List<PurchaseChildOrderChangePo> childPoToChangePo(List<PurchaseChildOrderPo> purchaseChildOrderPoList) {
        return purchaseChildOrderPoList.stream().map(childPo -> {
            final PurchaseChildOrderChangePo purchaseChildOrderChangePo = new PurchaseChildOrderChangePo();
            purchaseChildOrderChangePo.setPurchaseChildOrderId(childPo.getPurchaseChildOrderId());
            purchaseChildOrderChangePo.setPurchaseChildOrderNo(childPo.getPurchaseChildOrderNo());
            purchaseChildOrderChangePo.setPlaceOrderTime(LocalDateTime.now());
            return purchaseChildOrderChangePo;
        }).collect(Collectors.toList());
    }

    public static List<RawProductItemVo> rawPoListToVoList(List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                                           Map<String, String> skuEncodeMap) {
        return Optional.ofNullable(purchaseChildOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final RawProductItemVo rawProductItemVo = new RawProductItemVo();
                    rawProductItemVo.setPurchaseChildOrderRawId(po.getPurchaseChildOrderRawId());
                    rawProductItemVo.setVersion(po.getVersion());
                    rawProductItemVo.setSku(po.getSku());
                    rawProductItemVo.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    rawProductItemVo.setSkuBatchCode(po.getSkuBatchCode());
                    rawProductItemVo.setDeliveryCnt(po.getDeliveryCnt());
                    rawProductItemVo.setRawSupplier(po.getRawSupplier());
                    rawProductItemVo.setReceiptCnt(po.getReceiptCnt());
                    return rawProductItemVo;
                }).collect(Collectors.toList());
    }

    public static List<RawRemainItemVo> rawPoListToRemainVoList(List<PurchaseChildOrderRawPo> bomPurchaseChildOrderRawPoList,
                                                                List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                                                int qualityGoodsCnt, Map<String, String> skuEncodeMap) {
        final Map<String, List<PurchaseChildOrderRawPo>> skuRawPoListMap = Optional.ofNullable(purchaseChildOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderRawPo::getSku));


        return Optional.ofNullable(bomPurchaseChildOrderRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(po -> RawSupplier.HETE.equals(po.getRawSupplier())
                        || RawSupplier.OTHER_SUPPLIER.equals(po.getRawSupplier())
                        || RawSupplier.SUPPLIER.equals(po.getRawSupplier()))
                .map(po -> {
                    Integer deliveryCnt = 0;
                    final List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList1 = skuRawPoListMap.get(po.getSku());
                    if (CollectionUtils.isNotEmpty(purchaseChildOrderRawPoList1)) {
                        if (RawSupplier.HETE.equals(po.getRawSupplier())
                                || RawSupplier.OTHER_SUPPLIER.equals(po.getRawSupplier())) {
                            deliveryCnt = purchaseChildOrderRawPoList1.stream()
                                    .mapToInt(PurchaseChildOrderRawPo::getDeliveryCnt)
                                    .sum();
                        } else if (RawSupplier.SUPPLIER.equals(po.getRawSupplier())) {
                            deliveryCnt = purchaseChildOrderRawPoList1.stream()
                                    .mapToInt(rawPo -> rawPo.getActualConsumeCnt() + rawPo.getExtraCnt())
                                    .sum();
                        }
                    }
                    final RawRemainItemVo rawProductItemVo = new RawRemainItemVo();
                    rawProductItemVo.setPurchaseChildOrderRawId(po.getPurchaseChildOrderRawId());
                    rawProductItemVo.setVersion(po.getVersion());
                    rawProductItemVo.setSku(po.getSku());
                    rawProductItemVo.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    rawProductItemVo.setSkuBatchCode(po.getSkuBatchCode());
                    rawProductItemVo.setDeliveryCnt(deliveryCnt);
                    rawProductItemVo.setRawSupplier(po.getRawSupplier());
                    rawProductItemVo.setRemainCnt(deliveryCnt - qualityGoodsCnt * po.getDeliveryCnt());
                    return rawProductItemVo;
                }).collect(Collectors.toList());
    }

    public static PurchaseChildDetailVo poToChildDetailVo(PurchaseChildOrderPo purchaseChildOrderPo,
                                                          PurchaseParentOrderPo purchaseParentOrderPo,
                                                          List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                                                          List<PurchaseChildOrderVo> purchaseChildOrderVoList,
                                                          Map<String, Integer> modifySkuMap,
                                                          List<PurchaseChildOrderItemPo> skuMergeItemPoList,
                                                          Map<String, SupplierProductComparePo> supplierProductCompareMap,
                                                          Map<String, String> skuEncodeMap,
                                                          ProduceDataPo produceDataPo) {
        final PurchaseChildDetailVo purchaseChildDetailVo = new PurchaseChildDetailVo();
        purchaseChildDetailVo.setPurchaseChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        purchaseChildDetailVo.setVersion(purchaseChildOrderPo.getVersion());
        purchaseChildDetailVo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        purchaseChildDetailVo.setParentSkuCnt(purchaseParentOrderPo.getSkuCnt());
        purchaseChildDetailVo.setParentPurchaseTotal(purchaseParentOrderPo.getPurchaseTotal());
        purchaseChildDetailVo.setPurchaseParentOrderStatus(purchaseParentOrderPo.getPurchaseParentOrderStatus());
        purchaseChildDetailVo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildDetailVo.setSkuCnt(purchaseChildOrderPo.getSkuCnt());
        purchaseChildDetailVo.setPurchaseTotal(purchaseChildOrderPo.getPurchaseTotal());
        final PurchaseOrderStatus purchaseOrderStatus = purchaseChildOrderPo.getPurchaseOrderStatus();
        purchaseChildDetailVo.setPurchaseOrderStatus(purchaseOrderStatus);
        purchaseChildDetailVo.setPrePurchaseOrderStatus(purchaseOrderStatus.preStatus());
        purchaseChildDetailVo.setAfterPurchaseOrderStatus(purchaseOrderStatus.afterStatus());
        purchaseChildDetailVo.setPrePurchaseOrderStatusList(purchaseOrderStatus.preStatusList());
        purchaseChildDetailVo.setAfterPurchaseOrderStatusList(purchaseOrderStatus.afterStatusList());
        purchaseChildDetailVo.setSpu(purchaseChildOrderPo.getSpu());
        purchaseChildDetailVo.setWarehouseCode(purchaseChildOrderPo.getWarehouseCode());
        purchaseChildDetailVo.setWarehouseName(purchaseChildOrderPo.getWarehouseName());
        purchaseChildDetailVo.setWarehouseTypeList(FormatStringUtil.string2List(purchaseChildOrderPo.getWarehouseTypes(), ","));
        purchaseChildDetailVo.setExpectedOnShelvesDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
        purchaseChildDetailVo.setPlatform(purchaseChildOrderPo.getPlatform());
        purchaseChildDetailVo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        purchaseChildDetailVo.setSupplierName(purchaseChildOrderPo.getSupplierName());
        purchaseChildDetailVo.setOrderRemarks(purchaseChildOrderPo.getOrderRemarks());
        purchaseChildDetailVo.setPurchaseBizType(purchaseChildOrderPo.getPurchaseBizType());
        purchaseChildDetailVo.setIsDirectSend(purchaseChildOrderPo.getIsDirectSend());
        purchaseChildDetailVo.setIsUploadOverseasMsg(purchaseChildOrderPo.getIsUploadOverseasMsg());
        purchaseChildDetailVo.setUndeliveredCnt(purchaseParentOrderPo.getUndeliveredCnt());
        purchaseChildDetailVo.setShippableCnt(purchaseChildOrderPo.getShippableCnt());
        purchaseChildDetailVo.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType());
        purchaseChildDetailVo.setPromiseDate(purchaseChildOrderPo.getPromiseDate());
        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPoList.get(0);
        purchaseChildDetailVo.setSku(purchaseChildOrderItemPo.getSku());
        purchaseChildDetailVo.setSkuEncode(skuEncodeMap.get(purchaseChildOrderItemPo.getSku()));
        if (null != produceDataPo) {
            purchaseChildDetailVo.setRawManage(produceDataPo.getRawManage());
        } else {
            purchaseChildDetailVo.setRawManage(BooleanType.FALSE);
        }

        final List<PurchaseChildDetailVo.ChildOrderPurchaseItem> childOrderPurchaseItemList = purchaseChildOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseChildDetailVo.ChildOrderPurchaseItem childOrderPurchaseItem = new PurchaseChildDetailVo.ChildOrderPurchaseItem();
                    childOrderPurchaseItem.setPurchaseChildOrderItemId(po.getPurchaseChildOrderItemId());
                    childOrderPurchaseItem.setVersion(po.getVersion());
                    childOrderPurchaseItem.setSku(po.getSku());
                    childOrderPurchaseItem.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    childOrderPurchaseItem.setSkuBatchCode(po.getSkuBatchCode());
                    childOrderPurchaseItem.setVariantProperties(po.getVariantProperties());
                    childOrderPurchaseItem.setPurchasePrice(po.getPurchasePrice());
                    childOrderPurchaseItem.setDiscountType(po.getDiscountType());
                    childOrderPurchaseItem.setSubstractPrice(po.getSubstractPrice());
                    childOrderPurchaseItem.setSettlePrice(po.getSettlePrice());
                    childOrderPurchaseItem.setPurchaseCnt(po.getPurchaseCnt());
                    childOrderPurchaseItem.setInitPurchaseCnt(po.getInitPurchaseCnt());
                    childOrderPurchaseItem.setDeliverCnt(po.getDeliverCnt());
                    childOrderPurchaseItem.setQualityGoodsCnt(po.getQualityGoodsCnt());
                    childOrderPurchaseItem.setDefectiveGoodsCnt(po.getDefectiveGoodsCnt());
                    childOrderPurchaseItem.setModifyCnt(modifySkuMap.get(po.getSku()));

                    SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseChildOrderPo.getSupplierCode() + po.getSku());
                    if (null != supplierProductComparePo) {
                        childOrderPurchaseItem.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    }

                    return childOrderPurchaseItem;
                }).sorted(Comparator.comparing(PurchaseChildDetailVo.ChildOrderPurchaseItem::getSku))
                .collect(Collectors.toList());
        purchaseChildDetailVo.setPurchaseProductItemList(childOrderPurchaseItemList);

        purchaseChildOrderVoList.forEach(childVo -> childVo.setSkuEncode(skuEncodeMap.get(childVo.getSku())));
        purchaseChildDetailVo.setPurchaseChildOrderList(purchaseChildOrderVoList);

        final List<PurchaseParentItemVo> purchaseParentItemVoList = skuMergeItemPoList.stream()
                .map(po -> {
                    final PurchaseParentItemVo purchaseParentItemVo = new PurchaseParentItemVo();
                    purchaseParentItemVo.setSku(po.getSku());
                    purchaseParentItemVo.setPurchaseCnt(po.getPurchaseCnt());
                    purchaseParentItemVo.setDeliverCnt(po.getDeliverCnt());
                    purchaseParentItemVo.setQualityGoodsCnt(po.getQualityGoodsCnt());
                    purchaseParentItemVo.setDefectiveGoodsCnt(po.getDefectiveGoodsCnt());
                    purchaseParentItemVo.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    return purchaseParentItemVo;
                }).sorted(Comparator.comparing(PurchaseParentItemVo::getSku))
                .collect(Collectors.toList());
        purchaseChildDetailVo.setPurchaseParentItemList(purchaseParentItemVoList);

        return purchaseChildDetailVo;
    }

    public static PurchaseDeliverDetailVo detailPoToVo(PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                                       List<PurchaseDeliverOrderItemPo> poList,
                                                       Map<String, String> skuEncodeMap,
                                                       Map<String, String> supplierProductCompareMap) {

        final PurchaseDeliverDetailVo purchaseDeliverDetailVo = new PurchaseDeliverDetailVo();
        if (null == purchaseDeliverOrderPo) {
            return purchaseDeliverDetailVo;
        }
        purchaseDeliverDetailVo.setPurchaseDeliverOrderId(purchaseDeliverOrderPo.getPurchaseDeliverOrderId());
        purchaseDeliverDetailVo.setVersion(purchaseDeliverOrderPo.getVersion());
        purchaseDeliverDetailVo.setPurchaseDeliverOrderNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
        purchaseDeliverDetailVo.setDeliverOrderStatus(purchaseDeliverOrderPo.getDeliverOrderStatus());
        purchaseDeliverDetailVo.setLogistics(purchaseDeliverOrderPo.getLogistics());
        purchaseDeliverDetailVo.setTrackingNo(purchaseDeliverOrderPo.getTrackingNo());
        purchaseDeliverDetailVo.setSupplierCode(purchaseDeliverOrderPo.getSupplierCode());
        purchaseDeliverDetailVo.setSupplierName(purchaseDeliverOrderPo.getSupplierName());
        purchaseDeliverDetailVo.setPurchaseChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        purchaseDeliverDetailVo.setWarehouseCode(purchaseDeliverOrderPo.getWarehouseCode());
        purchaseDeliverDetailVo.setWarehouseName(purchaseDeliverOrderPo.getWarehouseName());
        purchaseDeliverDetailVo.setDeliverUsername(purchaseDeliverOrderPo.getDeliverUsername());
        purchaseDeliverDetailVo.setDeliverTime(purchaseDeliverOrderPo.getDeliverTime());
        purchaseDeliverDetailVo.setDeliverOrderType(purchaseDeliverOrderPo.getDeliverOrderType());
        purchaseDeliverDetailVo.setShippingMarkNo(purchaseDeliverOrderPo.getShippingMarkNo());

        if (CollectionUtils.isEmpty(poList)) {
            return purchaseDeliverDetailVo;
        }

        final List<PurchaseDeliverDetailVo.PurchaseDeliverItem> itemList = poList.stream()
                .map(po -> {
                    final PurchaseDeliverDetailVo.PurchaseDeliverItem item = new PurchaseDeliverDetailVo.PurchaseDeliverItem();
                    item.setSku(po.getSku());
                    item.setSkuBatchCode(po.getSkuBatchCode());
                    item.setVariantProperties(po.getVariantProperties());
                    item.setDeliverCnt(po.getDeliverCnt());
                    item.setReceiptCnt(po.getReceiptCnt());
                    item.setQualityGoodsCnt(po.getQualityGoodsCnt());
                    item.setDefectiveGoodsCnt(po.getDefectiveGoodsCnt());
                    item.setSkuEncode(skuEncodeMap.get(po.getSku()));
                    item.setSupplierProductName(supplierProductCompareMap.get(po.getSku()));
                    return item;
                }).collect(Collectors.toList());
        purchaseDeliverDetailVo.setPurchaseDeliverItemList(itemList);

        return purchaseDeliverDetailVo;
    }

    public static List<ScmImageBo> createDtoToImageList(List<PurchaseProductDemandItemDto> purchaseProductDemandItemList) {
        return purchaseProductDemandItemList.stream()
                .filter(item -> CollectionUtils.isNotEmpty(item.getFileCodeList()))
                .map(item -> {
                    final ScmImageBo scmImageBo = new ScmImageBo();
                    scmImageBo.setImageBizId(item.getPurchaseParentOrderItemId());
                    scmImageBo.setFileCodeList(item.getFileCodeList());

                    return scmImageBo;
                }).collect(Collectors.toList());
    }

    public static PurchaseModifyOrderPo modifyDtoToModifyPo(PurchaseModifyDto dto) {
        final PurchaseModifyOrderPo purchaseModifyOrderPo = new PurchaseModifyOrderPo();
        purchaseModifyOrderPo.setDownReturnOrderNo(dto.getDownReturnOrderNo());
        purchaseModifyOrderPo.setPurchaseChildOrderNo(dto.getPurchaseChildOrderNo());
        purchaseModifyOrderPo.setDownReturnOrderStatus(ReceiptOrderStatus.WAIT_RECEIVE);

        return purchaseModifyOrderPo;
    }

    public static List<PurchaseModifyOrderItemPo> modifyDtoToModifyItemPoList(List<PurchaseModifySkuDto> purchaseModifySkuList,
                                                                              String downReturnOrderNo) {
        return purchaseModifySkuList.stream()
                .map(dto -> {
                    final PurchaseModifyOrderItemPo purchaseModifyOrderItemPo = new PurchaseModifyOrderItemPo();
                    purchaseModifyOrderItemPo.setDownReturnOrderNo(downReturnOrderNo);
                    purchaseModifyOrderItemPo.setSku(dto.getSku());
                    purchaseModifyOrderItemPo.setPurchaseCnt(dto.getPurchaseCnt());
                    purchaseModifyOrderItemPo.setNewSku(dto.getNewSku());
                    purchaseModifyOrderItemPo.setNewPurchaseCnt(dto.getNewPurchaseCnt());
                    return purchaseModifyOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static List<PurchaseModifyVo> modifyPoToVo(List<PurchaseModifyOrderPo> purchaseModifyOrderPoList,
                                                      List<PurchaseModifyOrderItemPo> purchaseModifyOrderItemPoList) {
        final List<PurchaseModifyVo> purchaseModifyVoList = purchaseModifyOrderPoList.stream()
                .map(po -> {
                    final PurchaseModifyVo purchaseModifyVo = new PurchaseModifyVo();
                    purchaseModifyVo.setDownReturnOrderNo(po.getDownReturnOrderNo());
                    purchaseModifyVo.setDownReturnOrderStatus(po.getDownReturnOrderStatus());
                    purchaseModifyVo.setCreateTime(po.getCreateTime());
                    purchaseModifyVo.setCreateUsername(po.getCreateUsername());
                    return purchaseModifyVo;
                }).collect(Collectors.toList());

        final List<PurchaseModifyItemVo> itemVoList = purchaseModifyOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseModifyItemVo purchaseModifyItemVo = new PurchaseModifyItemVo();
                    purchaseModifyItemVo.setDownReturnOrderNo(po.getDownReturnOrderNo());
                    purchaseModifyItemVo.setSku(po.getSku());
                    purchaseModifyItemVo.setPurchaseCnt(po.getPurchaseCnt());
                    purchaseModifyItemVo.setNewSku(po.getNewSku());
                    purchaseModifyItemVo.setNewPurchaseCnt(po.getNewPurchaseCnt());
                    return purchaseModifyItemVo;
                }).collect(Collectors.toList());

        final Map<String, List<PurchaseModifyItemVo>> itemVoMap = itemVoList.stream()
                .collect(Collectors.groupingBy(PurchaseModifyItemVo::getDownReturnOrderNo));

        purchaseModifyVoList.forEach(vo -> {
            final List<PurchaseModifyItemVo> purchaseModifyItemVoList = itemVoMap.get(vo.getDownReturnOrderNo());
            vo.setPurchaseModifyItemList(purchaseModifyItemVoList);
        });

        return purchaseModifyVoList;
    }

    public static List<RawDeliverVo.DeliveryOrderVo> purchaseOrderDeliverBoToRawDeliverVo(List<RawDeliverOrderBo> rawDeliverOrderBoList) {
        if (CollectionUtils.isEmpty(rawDeliverOrderBoList)) {
            return Collections.emptyList();
        }

        return rawDeliverOrderBoList.stream()
                .map(bo -> {
                    final RawDeliverVo.DeliveryOrderVo deliveryOrderVo = new RawDeliverVo.DeliveryOrderVo();
                    deliveryOrderVo.setDeliveryOrderNo(bo.getDeliveryOrderNo());
                    deliveryOrderVo.setDeliveryAmount(bo.getDeliveryAmount());
                    deliveryOrderVo.setDeliveryState(bo.getDeliveryState());
                    deliveryOrderVo.setWarehouseName(bo.getWarehouseName());
                    deliveryOrderVo.setSkuBatchCode(bo.getSkuBatchCode());
                    return deliveryOrderVo;
                }).collect(Collectors.toList());
    }

    public static List<RawReceiveOrderVo.ReceiveOrderVo> rawReceiveOrderBoToReceiveOrderVo(List<RawReceiveOrderBo> rawReceiveOrderBoList) {
        if (CollectionUtils.isEmpty(rawReceiveOrderBoList)) {
            return Collections.emptyList();
        }

        return rawReceiveOrderBoList.stream()
                .map(bo -> {
                    final RawReceiveOrderVo.ReceiveOrderVo receiveOrderVo = new RawReceiveOrderVo.ReceiveOrderVo();
                    receiveOrderVo.setPurchaseRawReceiptOrderNo(bo.getReceiveOrderNo());
                    receiveOrderVo.setReceiptCnt(bo.getReceiveAmount());
                    receiveOrderVo.setReceiptOrderStatus(bo.getReceiveOrderState());
                    receiveOrderVo.setWarehouseCode(bo.getWarehouseCode());
                    receiveOrderVo.setWarehouseName(bo.getWarehouseName());
                    receiveOrderVo.setSkuBatchCode(bo.getSkuBatchCode());
                    receiveOrderVo.setSku(bo.getSku());
                    return receiveOrderVo;
                }).collect(Collectors.toList());

    }

    public static List<PurchaseChildOrderVo> purchaseChildPoToVo(List<PurchaseChildOrderPo> purchaseChildOrderPoList,
                                                                 List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                                                                 Map<String, String> skuEncodeMap) {
        if (CollectionUtils.isEmpty(purchaseChildOrderPoList) || CollectionUtils.isEmpty(purchaseChildOrderItemPoList)) {
            return Collections.emptyList();
        }
        final Map<String, List<PurchaseChildOrderItemPo>> purchaseChildNoItemMap = purchaseChildOrderItemPoList.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo));

        return purchaseChildOrderPoList.stream()
                .map(po -> {
                    final PurchaseChildOrderVo purchaseChildOrderVo = new PurchaseChildOrderVo();
                    purchaseChildOrderVo.setPurchaseChildOrderId(po.getPurchaseChildOrderId());
                    purchaseChildOrderVo.setVersion(po.getVersion());
                    purchaseChildOrderVo.setPurchaseChildOrderNo(po.getPurchaseChildOrderNo());
                    purchaseChildOrderVo.setPurchaseOrderStatus(po.getPurchaseOrderStatus());
                    purchaseChildOrderVo.setSupplierCode(po.getSupplierCode());
                    purchaseChildOrderVo.setSupplierName(po.getSupplierName());
                    purchaseChildOrderVo.setTotalSettlePrice(po.getTotalSettlePrice());
                    purchaseChildOrderVo.setPurchaseTotal(po.getPurchaseTotal());
                    purchaseChildOrderVo.setPurchaseBizType(po.getPurchaseBizType());
                    purchaseChildOrderVo.setDeleteStatus(po.getDelTimestamp());
                    purchaseChildOrderVo.setPlatform(po.getPlatform());
                    purchaseChildOrderVo.setOrderRemarks(po.getOrderRemarks());
                    purchaseChildOrderVo.setWarehouseCode(po.getWarehouseCode());
                    purchaseChildOrderVo.setWarehouseName(po.getWarehouseName());
                    purchaseChildOrderVo.setWarehouseTypeList(FormatStringUtil.string2List(po.getWarehouseTypes(), ","));
                    purchaseChildOrderVo.setExpectedOnShelvesDate(po.getExpectedOnShelvesDate());


                    final List<PurchaseProductItemVo> purchaseProductItemList = Optional.ofNullable(purchaseChildNoItemMap.get(po.getPurchaseChildOrderNo()))
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(itemPo -> {
                                final PurchaseProductItemVo purchaseProductItemVo = new PurchaseProductItemVo();
                                purchaseProductItemVo.setPurchaseChildOrderItemId(itemPo.getPurchaseChildOrderItemId());
                                purchaseProductItemVo.setVersion(itemPo.getVersion());
                                purchaseProductItemVo.setSku(itemPo.getSku());
                                purchaseProductItemVo.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                                purchaseProductItemVo.setVariantProperties(itemPo.getVariantProperties());
                                purchaseProductItemVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                                purchaseProductItemVo.setPurchasePrice(itemPo.getPurchasePrice());
                                purchaseProductItemVo.setDiscountType(itemPo.getDiscountType());
                                purchaseProductItemVo.setSubstractPrice(itemPo.getSubstractPrice());
                                purchaseProductItemVo.setSettlePrice(itemPo.getSettlePrice());
                                return purchaseProductItemVo;
                            }).collect(Collectors.toList());

                    purchaseChildOrderVo.setPurchaseProductItemList(purchaseProductItemList);
                    return purchaseChildOrderVo;
                }).collect(Collectors.toList());
    }

    public static List<PurchaseDemandRawVo> rawAndSkuStockToVo(List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                                               List<SkuInventoryVo> skuInventoryList,
                                                               Map<String, String> skuEncodeMap) {
        final Map<String, Integer> skuDeliveryCntMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.groupingBy(po -> po.getSku() + "," + po.getRawWarehouseCode(),
                        Collectors.summingInt(PurchaseChildOrderRawPo::getDeliveryCnt)));

        final Map<String, Integer> skuInventory = skuInventoryList.stream()
                .collect(Collectors.toMap(vo -> vo.getSkuCode() + "," + vo.getWarehouseCode(), SkuInventoryVo::getInStockAmount));

        final Map<String, PurchaseChildOrderRawPo> rawWarehousePoMap = purchaseChildOrderRawPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderRawPo::getRawWarehouseCode,
                        Function.identity(), (item1, item2) -> item1));

        return skuDeliveryCntMap.keySet()
                .stream()
                .map(skuWarehouse -> {
                    final String[] split = skuWarehouse.split(",");
                    final String sku = split[0];
                    final String rawWarehouseCode = split[1];
                    final PurchaseChildOrderRawPo purchaseChildOrderRawPo = rawWarehousePoMap.get(rawWarehouseCode);
                    final PurchaseDemandRawVo purchaseDemandRawVo = new PurchaseDemandRawVo();
                    purchaseDemandRawVo.setSku(sku);
                    purchaseDemandRawVo.setSkuEncode(skuEncodeMap.get(sku));
                    purchaseDemandRawVo.setSkuStock(skuInventory.get(skuWarehouse));
                    purchaseDemandRawVo.setDeliveryCnt(skuDeliveryCntMap.get(skuWarehouse));
                    if (null != purchaseChildOrderRawPo) {
                        purchaseDemandRawVo.setRawWarehouseName(purchaseChildOrderRawPo.getRawWarehouseName());
                        purchaseDemandRawVo.setRawSupplier(purchaseChildOrderRawPo.getRawSupplier());
                    }
                    return purchaseDemandRawVo;
                }).collect(Collectors.toList());
    }

    /**
     * 将收货单信息、质检单信息和不合格质检明细信息转换为WMS详情对象。
     *
     * @param receiveOrderList   收货单信息列表
     * @param qcOrderPos         质检单信息列表
     * @param notPassQcDetailPos 不合格质检明细信息列表
     * @return 包含WMS详情信息的WmsDetailVo对象
     */
    public static WmsDetailVo receiveOrderListToWmsDetailVoList(List<ReceiveOrderForScmVo> receiveOrderList,
                                                                List<QcOrderPo> qcOrderPos,
                                                                List<QcDetailPo> notPassQcDetailPos) {
        if (CollectionUtils.isEmpty(receiveOrderList)) {
            return new WmsDetailVo();
        }
        final WmsDetailVo wmsDetailVo = new WmsDetailVo();
        List<WmsReceiveDetailVo> wmsReceiveDetailVoList = new ArrayList<>();
        List<WmsQcDetailVo> allWmsReceiveDetailVoList = new ArrayList<>();
        List<WmsOnShelfDetailVo> allWmsOnShelfDetailList = new ArrayList<>();

        for (ReceiveOrderForScmVo receiveOrder : receiveOrderList) {
            final String receiveOrderNo = receiveOrder.getReceiveOrderNo();

            final WmsReceiveDetailVo wmsReceiveDetailVo = new WmsReceiveDetailVo();
            wmsReceiveDetailVo.setReceiveOrderNo(receiveOrderNo);
            wmsReceiveDetailVo.setReceiveOrderState(receiveOrder.getReceiveOrderState());
            wmsReceiveDetailVo.setFinishReceiveTime(receiveOrder.getFinishReceiveTime());
            wmsReceiveDetailVo.setReceiveAmount(receiveOrder.getReceiveAmount());
            wmsReceiveDetailVoList.add(wmsReceiveDetailVo);

            // 通过收货单匹配质检单信息
            List<QcOrderPo> matchQcOrderPos = qcOrderPos.stream()
                    .filter(qcOrderPo -> Objects.equals(receiveOrderNo, qcOrderPo.getReceiveOrderNo()))
                    .collect(Collectors.toList());

            final List<WmsQcDetailVo> wmsQcDetailList = Optional.of(matchQcOrderPos)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(qcOrderVo -> {
                        final String qcOrderNo = qcOrderVo.getQcOrderNo();

                        final WmsQcDetailVo wmsQcDetailVo = new WmsQcDetailVo();
                        // 质检主单信息
                        wmsQcDetailVo.setQcOrderNo(qcOrderNo);
                        wmsQcDetailVo.setQcState(qcOrderVo.getQcState());
                        wmsQcDetailVo.setCreateTime(qcOrderVo.getCreateTime());
                        wmsQcDetailVo.setQcAmount(qcOrderVo.getQcAmount());

                        // 质检不合格明细信息
                        final List<QcDetailPo> matchQcDetailPos = notPassQcDetailPos.stream()
                                .filter(qcDetailPo -> Objects.equals(qcOrderNo, qcDetailPo.getQcOrderNo()))
                                .collect(Collectors.toList());

                        final List<WmsNotPassQcDetail> notPassQcDetailList
                                = CollectionUtils.isEmpty(matchQcDetailPos) ? Collections.emptyList() : matchQcDetailPos.stream()
                                .map(notPassQcDetail -> {
                                    WmsNotPassQcDetail wmsNotPassQcDetail = new WmsNotPassQcDetail();
                                    // 设置质检单详情id主键
                                    wmsNotPassQcDetail.setQcDetailId(notPassQcDetail.getQcDetailId());
                                    // 设置批次码
                                    wmsNotPassQcDetail.setBatchCode(notPassQcDetail.getBatchCode());
                                    // 设置赫特sku
                                    wmsNotPassQcDetail.setSkuCode(notPassQcDetail.getSkuCode());
                                    // 设置不通过数量
                                    wmsNotPassQcDetail.setNotPassAmount(notPassQcDetail.getNotPassAmount());
                                    // 设置质检不合格原因
                                    wmsNotPassQcDetail.setQcNotPassedReason(notPassQcDetail.getQcNotPassedReason());
                                    // 设置备注
                                    wmsNotPassQcDetail.setRemark(notPassQcDetail.getRemark());
                                    // 设置图片文件ID列表
                                    wmsNotPassQcDetail.setPictureUrlList(Arrays.asList(notPassQcDetail.getPictureIds()
                                            .split(",")));
                                    return wmsNotPassQcDetail;
                                })
                                .collect(Collectors.toList());
                        wmsQcDetailVo.setNotPassQcDetailList(notPassQcDetailList);
                        return wmsQcDetailVo;
                    })
                    .collect(Collectors.toList());
            allWmsReceiveDetailVoList.addAll(wmsQcDetailList);

            final List<WmsOnShelfDetailVo> wmsOnShelfDetailList = Optional.ofNullable(receiveOrder.getOnShelfList())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(onShelfOrderVo -> {
                        final WmsOnShelfDetailVo wmsOnShelfDetailVo = new WmsOnShelfDetailVo();
                        wmsOnShelfDetailVo.setOnShelvesOrderNo(onShelfOrderVo.getOnShelvesOrderNo());
                        wmsOnShelfDetailVo.setOnShelfState(onShelfOrderVo.getOnShelfState());
                        wmsOnShelfDetailVo.setCreateTime(onShelfOrderVo.getCreateTime());
                        wmsOnShelfDetailVo.setOnShelvesAmount(onShelfOrderVo.getOnShelvesAmount());
                        return wmsOnShelfDetailVo;
                    })
                    .collect(Collectors.toList());
            allWmsOnShelfDetailList.addAll(wmsOnShelfDetailList);

        }

        wmsDetailVo.setWmsReceiveDetailList(wmsReceiveDetailVoList);
        wmsDetailVo.setWmsQcDetailList(allWmsReceiveDetailVoList);
        wmsDetailVo.setWmsOnShelfDetailList(allWmsOnShelfDetailList);

        return wmsDetailVo;
    }

    public static PurchaseChildOrderPo getInitSplitSupply(PurchaseSplitSupplyDto dto, String purchaseChildOrderNo,
                                                          PurchaseChildOrderPo purchaseChildOrderPo, SupplierPo supplierPo,
                                                          SplitType splitType) {
        final PurchaseChildOrderPo newPurchaseChildOrderPo = new PurchaseChildOrderPo();
        newPurchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
        newPurchaseChildOrderPo.setSourcePurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        newPurchaseChildOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        newPurchaseChildOrderPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        newPurchaseChildOrderPo.setSampleChildOrderNo(purchaseChildOrderPo.getSampleChildOrderNo());
        newPurchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_SCHEDULING);
        newPurchaseChildOrderPo.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType());
        newPurchaseChildOrderPo.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder());
        newPurchaseChildOrderPo.setIsNormalOrder(purchaseChildOrderPo.getIsNormalOrder());
        newPurchaseChildOrderPo.setSpu(purchaseChildOrderPo.getSpu());
        newPurchaseChildOrderPo.setPlatform(purchaseChildOrderPo.getPlatform());
        newPurchaseChildOrderPo.setPurchaseBizType(purchaseChildOrderPo.getPurchaseBizType());
        newPurchaseChildOrderPo.setSupplierCode(supplierPo.getSupplierCode());
        newPurchaseChildOrderPo.setSupplierName(supplierPo.getSupplierName());
        newPurchaseChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
        newPurchaseChildOrderPo.setWarehouseName(dto.getWarehouseName());
        newPurchaseChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));
        newPurchaseChildOrderPo.setOrderRemarks(dto.getOrderRemarks());
        newPurchaseChildOrderPo.setSkuCnt(purchaseChildOrderPo.getSkuCnt());
        newPurchaseChildOrderPo.setPurchaseTotal(dto.getSupplyPurchaseCnt());
        newPurchaseChildOrderPo.setExpectedOnShelvesDate(dto.getExpectedOnShelvesDate());
        newPurchaseChildOrderPo.setIsDirectSend(purchaseChildOrderPo.getIsDirectSend());
        newPurchaseChildOrderPo.setIsUploadOverseasMsg(purchaseChildOrderPo.getIsUploadOverseasMsg());
        newPurchaseChildOrderPo.setShippableCnt(dto.getSupplyPurchaseCnt());
        if (SplitType.SUPPLIER_SPLIT.equals(splitType)) {
            newPurchaseChildOrderPo.setPlaceOrderUser(purchaseChildOrderPo.getPlaceOrderUser());
            newPurchaseChildOrderPo.setPlaceOrderUsername(purchaseChildOrderPo.getPlaceOrderUsername());
        } else {
            newPurchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
            newPurchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        }

        newPurchaseChildOrderPo.setOrderSource(purchaseChildOrderPo.getOrderSource());

        return newPurchaseChildOrderPo;
    }

    public static PurchaseChildOrderPo getInitSplitSupply(String purchaseChildOrderNo,
                                                          PurchaseChildOrderPo purchaseChildOrderPo,
                                                          SupplierPo supplierPo,
                                                          Integer supplyPurchaseCnt,
                                                          SplitType splitType) {
        final PurchaseChildOrderPo newPurchaseChildOrderPo = new PurchaseChildOrderPo();
        newPurchaseChildOrderPo.setPromiseDateChg(BooleanType.FALSE);
        newPurchaseChildOrderPo.setSourcePurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        newPurchaseChildOrderPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        newPurchaseChildOrderPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        newPurchaseChildOrderPo.setSampleChildOrderNo(purchaseChildOrderPo.getSampleChildOrderNo());
        newPurchaseChildOrderPo.setPurchaseOrderStatus(PurchaseOrderStatus.WAIT_SCHEDULING);
        newPurchaseChildOrderPo.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType());
        newPurchaseChildOrderPo.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder());
        newPurchaseChildOrderPo.setIsNormalOrder(purchaseChildOrderPo.getIsNormalOrder());
        newPurchaseChildOrderPo.setSpu(purchaseChildOrderPo.getSpu());
        newPurchaseChildOrderPo.setPlatform(purchaseChildOrderPo.getPlatform());
        newPurchaseChildOrderPo.setPurchaseBizType(purchaseChildOrderPo.getPurchaseBizType());
        newPurchaseChildOrderPo.setSupplierCode(supplierPo.getSupplierCode());
        newPurchaseChildOrderPo.setSupplierName(supplierPo.getSupplierName());
        newPurchaseChildOrderPo.setWarehouseCode(purchaseChildOrderPo.getWarehouseCode());
        newPurchaseChildOrderPo.setWarehouseName(purchaseChildOrderPo.getWarehouseName());
        newPurchaseChildOrderPo.setWarehouseTypes(purchaseChildOrderPo.getWarehouseTypes());
        newPurchaseChildOrderPo.setOrderRemarks(purchaseChildOrderPo.getOrderRemarks());
        newPurchaseChildOrderPo.setSkuCnt(purchaseChildOrderPo.getSkuCnt());
        newPurchaseChildOrderPo.setPurchaseTotal(supplyPurchaseCnt);
        newPurchaseChildOrderPo.setExpectedOnShelvesDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
        newPurchaseChildOrderPo.setIsDirectSend(purchaseChildOrderPo.getIsDirectSend());
        newPurchaseChildOrderPo.setIsUploadOverseasMsg(purchaseChildOrderPo.getIsUploadOverseasMsg());
        newPurchaseChildOrderPo.setShippableCnt(supplyPurchaseCnt);
        if (SplitType.SUPPLIER_SPLIT.equals(splitType)) {
            newPurchaseChildOrderPo.setPlaceOrderUser(purchaseChildOrderPo.getPlaceOrderUser());
            newPurchaseChildOrderPo.setPlaceOrderUsername(purchaseChildOrderPo.getPlaceOrderUsername());
        } else {
            newPurchaseChildOrderPo.setPlaceOrderUser(GlobalContext.getUserKey());
            newPurchaseChildOrderPo.setPlaceOrderUsername(GlobalContext.getUsername());
        }

        newPurchaseChildOrderPo.setOrderSource(purchaseChildOrderPo.getOrderSource());

        return newPurchaseChildOrderPo;
    }


    public static PurchaseChildOrderItemPo getInitSplitSupplyItem(Integer supplyPurchaseCnt, String purchaseChildOrderNo,
                                                                  PurchaseChildOrderItemPo purchaseChildOrderItemPo,
                                                                  Map<String, String> skuBatchMap) {

        final PurchaseChildOrderItemPo newPurchaseChildOrderItemPo = new PurchaseChildOrderItemPo();
        newPurchaseChildOrderItemPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        newPurchaseChildOrderItemPo.setPurchaseParentOrderNo(purchaseChildOrderItemPo.getPurchaseParentOrderNo());
        newPurchaseChildOrderItemPo.setSku(purchaseChildOrderItemPo.getSku());
        newPurchaseChildOrderItemPo.setSkuBatchCode(skuBatchMap.get(purchaseChildOrderItemPo.getSku()));
        newPurchaseChildOrderItemPo.setVariantProperties(purchaseChildOrderItemPo.getVariantProperties());
        newPurchaseChildOrderItemPo.setPurchaseCnt(supplyPurchaseCnt);
        newPurchaseChildOrderItemPo.setInitPurchaseCnt(supplyPurchaseCnt);
        newPurchaseChildOrderItemPo.setPurchasePrice(purchaseChildOrderItemPo.getPurchasePrice());
        newPurchaseChildOrderItemPo.setDiscountType(purchaseChildOrderItemPo.getDiscountType());
        newPurchaseChildOrderItemPo.setSubstractPrice(purchaseChildOrderItemPo.getSubstractPrice());
        newPurchaseChildOrderItemPo.setDeliverCnt(0);
        newPurchaseChildOrderItemPo.setQualityGoodsCnt(0);
        newPurchaseChildOrderItemPo.setDefectiveGoodsCnt(0);
        newPurchaseChildOrderItemPo.setSettlePrice(purchaseChildOrderItemPo.getSettlePrice());
        newPurchaseChildOrderItemPo.setUndeliveredCnt(supplyPurchaseCnt);
        return newPurchaseChildOrderItemPo;
    }

    public static List<PurchaseChildPreConfirmExportVo> purchaseProductSearchVoToExportVo(List<PurchaseProductSearchVo> records,
                                                                                          Map<String, String> platCodeNameMap) {
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }

        return records.stream().map(record -> {
            final PurchaseChildPreConfirmExportVo vo = new PurchaseChildPreConfirmExportVo();
            vo.setPlatformName(platCodeNameMap.get(record.getPlatform()));
            vo.setPurchaseTotal(record.getPurchaseTotal());
            vo.setExpectedOnShelvesDate(ScmTimeUtil.localDateTimeToStr(record.getExpectedOnShelvesDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            vo.setDeliverDate(ScmTimeUtil.localDateTimeToStr(record.getDeliverDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            vo.setPurchaseChildOrderNo(record.getPurchaseChildOrderNo());
            vo.setWarehouseCode(record.getWarehouseCode());

            return vo;
        }).collect(Collectors.toList());
    }

    public static List<PurchaseDeliverSimpleVo> deliverPoToSimpleVo(List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList,
                                                                    Map<String, Integer> purchaseDeliverOrderNoReceiptCntMap) {
        if (CollectionUtils.isEmpty(purchaseDeliverOrderPoList)) {
            return new ArrayList<>();
        }

        return purchaseDeliverOrderPoList.stream().map(purchaseDeliverOrderPo -> {
            final PurchaseDeliverSimpleVo purchaseDeliverSimpleVo = new PurchaseDeliverSimpleVo();
            purchaseDeliverSimpleVo.setPurchaseDeliverOrderNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
            purchaseDeliverSimpleVo.setDeliverOrderStatus(purchaseDeliverOrderPo.getDeliverOrderStatus());
            purchaseDeliverSimpleVo.setDeliverCnt(purchaseDeliverOrderPo.getDeliverCnt());
            purchaseDeliverSimpleVo.setReceiptCnt(purchaseDeliverOrderNoReceiptCntMap.getOrDefault(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), 0));
            purchaseDeliverSimpleVo.setPurchaseReceiptOrderNo(purchaseDeliverOrderPo.getPurchaseReceiptOrderNo());
            return purchaseDeliverSimpleVo;
        }).collect(Collectors.toList());
    }

    public static PurchaseParentOrderItemPo itemDtoToRawPurchaseParentItemPo(RawProductItemDto rawDto, String purchaseParentOrderNo) {
        PurchaseParentOrderItemPo purchaseParentOrderItemPo = new PurchaseParentOrderItemPo();

        purchaseParentOrderItemPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseParentOrderItemPo.setSku(rawDto.getSku());
        purchaseParentOrderItemPo.setPurchaseCnt(rawDto.getDeliveryCnt());

        return purchaseParentOrderItemPo;
    }

    public static List<ConfirmCommissioningMsgVo> rawPoToConfirmMsgVoList(List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                                                          List<SupplierInventoryPo> supplierInventoryPoList) {
        if (CollectionUtils.isEmpty(purchaseChildOrderRawPoList)) {
            return Collections.emptyList();
        }

        // 因为这个list是根据单个供应商查询的，所以直接根据sku分组，如果出现重复，则说明数据有问题
        final Map<String, Integer> skuInventoryMap = Optional.ofNullable(supplierInventoryPoList)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(SupplierInventoryPo::getSku,
                        po -> po.getSelfProvideInventory() + po.getStockUpInventory()));

        return purchaseChildOrderRawPoList.stream().map(rawPo -> {
            final ConfirmCommissioningMsgVo confirmCommissioningMsgVo = new ConfirmCommissioningMsgVo();
            confirmCommissioningMsgVo.setSku(rawPo.getSku());
            confirmCommissioningMsgVo.setExpectedConsumeCnt(rawPo.getDeliveryCnt());
            if (RawSupplier.SUPPLIER.equals(rawPo.getRawSupplier())) {
                confirmCommissioningMsgVo.setTotalInventory(skuInventoryMap.getOrDefault(rawPo.getSku(), 0));
            }
            confirmCommissioningMsgVo.setRawExtra(rawPo.getRawExtra());
            confirmCommissioningMsgVo.setRawSupplier(rawPo.getRawSupplier());
            return confirmCommissioningMsgVo;
        }).collect(Collectors.toList());
    }

    public static List<PurchaseDeliverRawVo> rawPoAndInventoryToVo(List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                                                                   Map<String, Integer> skuInventoryCntMap) {

        return purchaseChildOrderRawPoList.stream()
                .map(rawPo -> {
                    final PurchaseDeliverRawVo purchaseDeliverRawVo = new PurchaseDeliverRawVo();
                    purchaseDeliverRawVo.setSku(rawPo.getSku());
                    purchaseDeliverRawVo.setDeliveryCnt(rawPo.getDeliveryCnt());
                    purchaseDeliverRawVo.setActualConsumeCnt(rawPo.getActualConsumeCnt());
                    purchaseDeliverRawVo.setSupplierInventory(skuInventoryCntMap.getOrDefault(rawPo.getSku(), 0));
                    purchaseDeliverRawVo.setRawSupplier(rawPo.getRawSupplier());

                    return purchaseDeliverRawVo;
                }).collect(Collectors.toList());
    }

    public static PurchaseChildOrderRawDeliverPo rawPoToRawDeliverPo(PurchaseChildOrderRawPo rawPo,
                                                                     Integer deliveryCnt, String purchaseRawDeliverOrderNo,
                                                                     Long supplierInventoryRecordId, RawSupplier rawSupplier,
                                                                     BooleanType particularLocation) {
        final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = new PurchaseChildOrderRawDeliverPo();
        purchaseChildOrderRawDeliverPo.setPurchaseParentOrderNo(rawPo.getPurchaseParentOrderNo());
        purchaseChildOrderRawDeliverPo.setPurchaseChildOrderNo(rawPo.getPurchaseChildOrderNo());
        purchaseChildOrderRawDeliverPo.setSku(rawPo.getSku());
        purchaseChildOrderRawDeliverPo.setDeliveryCnt(deliveryCnt);
        purchaseChildOrderRawDeliverPo.setRawSupplier(rawSupplier);
        purchaseChildOrderRawDeliverPo.setPurchaseRawDeliverOrderNo(purchaseRawDeliverOrderNo);
        purchaseChildOrderRawDeliverPo.setSupplierInventoryRecordId(supplierInventoryRecordId);
        purchaseChildOrderRawDeliverPo.setDispenseCnt(deliveryCnt);
        purchaseChildOrderRawDeliverPo.setParticularLocation(particularLocation);
        // 默认不指定库位
        if (null == particularLocation) {
            purchaseChildOrderRawDeliverPo.setParticularLocation(BooleanType.FALSE);
        }
        return purchaseChildOrderRawDeliverPo;

    }

    public static PurchaseChildOrderRawDeliverPo rawPoToRawDeliverPo(String sku,
                                                                     PurchaseChildOrderPo purchaseChildOrderPo,
                                                                     Integer deliveryCnt, String purchaseRawDeliverOrderNo,
                                                                     Long supplierInventoryRecordId, RawSupplier rawSupplier,
                                                                     BooleanType particularLocation) {
        final PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo = new PurchaseChildOrderRawDeliverPo();
        purchaseChildOrderRawDeliverPo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
        purchaseChildOrderRawDeliverPo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseChildOrderRawDeliverPo.setSku(sku);
        purchaseChildOrderRawDeliverPo.setDeliveryCnt(deliveryCnt);
        purchaseChildOrderRawDeliverPo.setRawSupplier(rawSupplier);
        purchaseChildOrderRawDeliverPo.setPurchaseRawDeliverOrderNo(purchaseRawDeliverOrderNo);
        purchaseChildOrderRawDeliverPo.setSupplierInventoryRecordId(supplierInventoryRecordId);
        purchaseChildOrderRawDeliverPo.setDispenseCnt(deliveryCnt);
        purchaseChildOrderRawDeliverPo.setParticularLocation(particularLocation);
        // 默认不指定库位
        if (null == particularLocation) {
            purchaseChildOrderRawDeliverPo.setParticularLocation(BooleanType.FALSE);
        }
        return purchaseChildOrderRawDeliverPo;

    }

    public static PurchaseChildOrderRawPo newRawPoByOldRawPo(PurchaseChildOrderRawPo rawPo, PurchaseRawBizType purchaseRawBizType,
                                                             String purchaseParentOrderNo, String purchaseChildOrderNo) {
        final PurchaseChildOrderRawPo purchaseChildOrderRawPo = new PurchaseChildOrderRawPo();
        purchaseChildOrderRawPo.setPurchaseChildOrderRawId(rawPo.getPurchaseChildOrderRawId());
        purchaseChildOrderRawPo.setPurchaseParentOrderNo(purchaseParentOrderNo);
        purchaseChildOrderRawPo.setPurchaseChildOrderNo(purchaseChildOrderNo);
        purchaseChildOrderRawPo.setSku(rawPo.getSku());
        purchaseChildOrderRawPo.setSkuBatchCode(rawPo.getSkuBatchCode());
        purchaseChildOrderRawPo.setDeliveryCnt(rawPo.getDeliveryCnt());
        purchaseChildOrderRawPo.setPurchaseRawBizType(purchaseRawBizType);
        purchaseChildOrderRawPo.setRawSupplier(rawPo.getRawSupplier());
        purchaseChildOrderRawPo.setActualConsumeCnt(rawPo.getActualConsumeCnt());
        purchaseChildOrderRawPo.setExtraCnt(rawPo.getExtraCnt());
        purchaseChildOrderRawPo.setRawExtra(rawPo.getRawExtra());
        purchaseChildOrderRawPo.setDispenseCnt(rawPo.getDispenseCnt());
        purchaseChildOrderRawPo.setRawWarehouseCode(rawPo.getRawWarehouseCode());
        purchaseChildOrderRawPo.setRawWarehouseName(rawPo.getRawWarehouseName());
        return purchaseChildOrderRawPo;
    }
}
