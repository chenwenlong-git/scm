package com.hete.supply.scm.server.supplier.purchase.converter;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseRawReceiptExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSkuCntVo;
import com.hete.supply.scm.api.scm.entity.vo.RawReceiptExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDeliverDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderChangePo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseReturnSearchVo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.supplier.entity.dto.RawReturnProductItemDto;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgPo;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverCreateDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseDeliverCreateItemDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseRawCommitDto;
import com.hete.supply.scm.server.supplier.purchase.entity.dto.PurchaseReturnItemDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.*;
import com.hete.supply.scm.server.supplier.purchase.entity.vo.*;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.core.util.TimeZoneId;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/20 14:42
 */
@Slf4j
public class SupplierPurchaseConverter {

    public static List<PurchaseReturnVo> returnPoListToVoList(List<PurchaseReturnSearchVo> records,
                                                              Map<String, List<PurchaseSkuCntVo>> returnNoMap,
                                                              Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap,
                                                              Map<String, List<PurchaseReturnOrderItemPo>> returnNoItemListMap) {
        return records.stream()
                .map(vo -> {
                    final PurchaseReturnVo purchaseReturnVo = new PurchaseReturnVo();
                    purchaseReturnVo.setReturnOrderNo(vo.getReturnOrderNo());
                    purchaseReturnVo.setReturnType(vo.getReturnType());
                    purchaseReturnVo.setReturnOrderStatus(vo.getReturnOrderStatus());
                    purchaseReturnVo.setLogistics(vo.getLogistics());
                    purchaseReturnVo.setTrackingNo(vo.getTrackingNo());
                    purchaseReturnVo.setExpectedReturnCnt(vo.getExpectedReturnCnt());
                    purchaseReturnVo.setRealityReturnCnt(vo.getRealityReturnCnt());
                    purchaseReturnVo.setReceiptCnt(vo.getReceiptCnt());
                    purchaseReturnVo.setReceiptUser(vo.getReceiptUser());
                    purchaseReturnVo.setReceiptUsername(vo.getReceiptUsername());
                    purchaseReturnVo.setReceiptTime(vo.getReceiptTime());
                    purchaseReturnVo.setCreateUser(vo.getReturnCreateUser());
                    purchaseReturnVo.setCreateUsername(vo.getReturnCreateUsername());
                    purchaseReturnVo.setCreateTime(vo.getCreateTime());
                    purchaseReturnVo.setReturnUser(vo.getReturnUser());
                    purchaseReturnVo.setReturnUsername(vo.getReturnUsername());
                    purchaseReturnVo.setReturnTime(vo.getReturnTime());
                    purchaseReturnVo.setSupplierCode(vo.getSupplierCode());
                    purchaseReturnVo.setSupplierName(vo.getSupplierName());
                    purchaseReturnVo.setSkuList(returnNoMap.get(vo.getReturnOrderNo()));
                    purchaseReturnVo.setPurchaseChildOrderNo(vo.getPurchaseChildOrderNo());
                    purchaseReturnVo.setPlatform(vo.getPlatform());

                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderNoPoMap.get(vo.getPurchaseChildOrderNo());
                    if (null != purchaseChildOrderPo) {
                        purchaseReturnVo.setWarehouseCode(purchaseChildOrderPo.getWarehouseCode());
                        purchaseReturnVo.setPurchaseTotal(purchaseChildOrderPo.getPurchaseTotal());
                    }
                    final List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList = returnNoItemListMap.get(vo.getReturnOrderNo());
                    if (CollectionUtils.isNotEmpty(purchaseReturnOrderItemPoList)) {
                        final List<PurchaseReturnItemVo> purchaseReturnItemList = purchaseReturnOrderItemPoList.stream().map(itemPo -> {
                            final PurchaseReturnItemVo purchaseReturnItemVo = new PurchaseReturnItemVo();
                            purchaseReturnItemVo.setSku(itemPo.getSku());
                            purchaseReturnItemVo.setRealityReturnCnt(itemPo.getRealityReturnCnt());
                            return purchaseReturnItemVo;
                        }).collect(Collectors.toList());
                        purchaseReturnVo.setPurchaseReturnItemList(purchaseReturnItemList);
                    }

                    return purchaseReturnVo;
                }).collect(Collectors.toList());
    }

    public static PurchaseReturnDetailVo returnPoToDetailVo(PurchaseReturnOrderPo purchaseReturnOrderPo,
                                                            List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList,
                                                            Map<String, SupplierProductComparePo> supplierProductCompareMap,
                                                            Map<String, String> skuEncodeMap,
                                                            Map<String, BigDecimal> purchaseReturnSettlePriceMap) {
        final PurchaseReturnDetailVo purchaseReturnDetailVo = new PurchaseReturnDetailVo();
        purchaseReturnDetailVo.setPurchaseReturnOrderId(purchaseReturnOrderPo.getPurchaseReturnOrderId());
        purchaseReturnDetailVo.setVersion(purchaseReturnOrderPo.getVersion());
        purchaseReturnDetailVo.setReturnOrderNo(purchaseReturnOrderPo.getReturnOrderNo());
        purchaseReturnDetailVo.setReturnType(purchaseReturnOrderPo.getReturnType());
        purchaseReturnDetailVo.setLogistics(purchaseReturnOrderPo.getLogistics());
        purchaseReturnDetailVo.setTrackingNo(purchaseReturnOrderPo.getTrackingNo());
        purchaseReturnDetailVo.setSupplierCode(purchaseReturnOrderPo.getSupplierCode());
        purchaseReturnDetailVo.setSupplierName(purchaseReturnOrderPo.getSupplierName());
        purchaseReturnDetailVo.setReturnOrderStatus(purchaseReturnOrderPo.getReturnOrderStatus());
        purchaseReturnDetailVo.setNote(purchaseReturnOrderPo.getNote());
        purchaseReturnDetailVo.setPlatform(purchaseReturnOrderPo.getPlatform());

        final List<PurchaseReturnDetailVo.PurchaseReturnItem> purchaseReturnItemList = purchaseReturnOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseReturnDetailVo.PurchaseReturnItem item = new PurchaseReturnDetailVo.PurchaseReturnItem();
                    item.setPurchaseReturnOrderItemId(po.getPurchaseReturnOrderItemId());
                    item.setVersion(po.getVersion());
                    item.setSku(po.getSku());
                    item.setSkuEncode(po.getSkuEncode());
                    item.setSkuBatchCode(po.getSkuBatchCode());
                    item.setReturnBizNo(po.getReturnBizNo());
                    item.setRealityReturnCnt(po.getRealityReturnCnt());
                    item.setExpectedReturnCnt(po.getExpectedReturnCnt());
                    item.setReceiptCnt(po.getReceiptCnt());
                    item.setSettlePrice(po.getSettlePrice());

                    SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseReturnOrderPo.getSupplierCode() + po.getSku());
                    if (null != supplierProductComparePo) {
                        item.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    }

                    BigDecimal settleRecoOrderPrice = Optional.ofNullable(purchaseReturnSettlePriceMap.get(purchaseReturnOrderPo.getReturnOrderNo() + po.getSkuBatchCode()))
                            .orElse(BigDecimal.ZERO);
                    item.setSettleRecoOrderPrice(settleRecoOrderPrice);
                    if (null != item.getReceiptCnt() && ReturnOrderStatus.RECEIPTED.equals(purchaseReturnOrderPo.getReturnOrderStatus())) {
                        item.setSettleRecoOrderPriceTotal(settleRecoOrderPrice.multiply(new BigDecimal(item.getReceiptCnt())));
                    }

                    return item;
                }).collect(Collectors.toList());

        purchaseReturnDetailVo.setPurchaseReturnItemList(purchaseReturnItemList);

        return purchaseReturnDetailVo;
    }

    public static List<PurchaseReturnOrderItemPo> returnConfirmDtoListToPo(List<PurchaseReturnItemDto> purchaseReturnItemList) {

        return purchaseReturnItemList.stream()
                .map(dto -> {
                    final PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = new PurchaseReturnOrderItemPo();
                    purchaseReturnOrderItemPo.setPurchaseReturnOrderItemId(dto.getPurchaseReturnOrderItemId());
                    purchaseReturnOrderItemPo.setVersion(dto.getVersion());
                    purchaseReturnOrderItemPo.setReceiptCnt(dto.getReceiptCnt());
                    return purchaseReturnOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static PurchaseDeliverOrderPo deliverDtoToPo(PurchaseDeliverCreateDto dto, String deliverOrderNo,
                                                        PurchaseChildOrderPo purchaseChildOrderPo,
                                                        OverseasWarehouseMsgPo overseasWarehouseMsgPo) {
        final int totalDeliverCnt = dto.getPurchaseDeliverItemDtoList()
                .stream()
                .mapToInt(PurchaseDeliverCreateItemDto::getDeliverCnt)
                .sum();
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = new PurchaseDeliverOrderPo();
        purchaseDeliverOrderPo.setPurchaseDeliverOrderNo(deliverOrderNo);
        purchaseDeliverOrderPo.setDeliverOrderStatus(DeliverOrderStatus.WAIT_DELIVER);
        purchaseDeliverOrderPo.setDeliverCnt(totalDeliverCnt);
        purchaseDeliverOrderPo.setPurchaseChildOrderNo(dto.getPurchaseChildOrderNo());
        purchaseDeliverOrderPo.setWarehouseCode(purchaseChildOrderPo.getWarehouseCode());
        purchaseDeliverOrderPo.setWarehouseName(purchaseChildOrderPo.getWarehouseName());
        purchaseDeliverOrderPo.setWarehouseTypes(purchaseChildOrderPo.getWarehouseTypes());
        purchaseDeliverOrderPo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        purchaseDeliverOrderPo.setSupplierName(purchaseChildOrderPo.getSupplierName());
        purchaseDeliverOrderPo.setDeliverDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
        purchaseDeliverOrderPo.setIsDirectSend(purchaseChildOrderPo.getIsDirectSend());
        purchaseDeliverOrderPo.setDeliverOrderType(DeliverOrderType.BULK);
        purchaseDeliverOrderPo.setHasShippingMark(BooleanType.FALSE);
        if (null != overseasWarehouseMsgPo) {
            purchaseDeliverOrderPo.setShippingMarkNo(overseasWarehouseMsgPo.getOverseasShippingMarkNo());
        }

        return purchaseDeliverOrderPo;
    }

    public static List<PurchaseDeliverOrderItemPo> deliverDtoToItemPoList(List<PurchaseDeliverCreateItemDto> dtoList, String deliverOrderNo) {
        return dtoList.stream()
                .map(dto -> {
                    final PurchaseDeliverOrderItemPo itemPo = new PurchaseDeliverOrderItemPo();
                    itemPo.setPurchaseDeliverOrderNo(deliverOrderNo);
                    itemPo.setSku(dto.getSku());
                    itemPo.setVariantProperties(dto.getVariantProperties());
                    itemPo.setDeliverCnt(dto.getDeliverCnt());
                    itemPo.setSpu(dto.getSpu());
                    itemPo.setSkuBatchCode(dto.getSkuBatchCode());
                    itemPo.setPurchaseCnt(dto.getPurchaseCnt());
                    return itemPo;
                }).collect(Collectors.toList());
    }

    public static List<PurchaseRawReceiptVo> rawReceiptPoListToVoList(List<PurchaseRawReceiptOrderPo> records) {

        return records.stream()
                .map(record -> {
                    final PurchaseRawReceiptVo purchaseRawReceiptVo = new PurchaseRawReceiptVo();
                    purchaseRawReceiptVo.setPurchaseRawReceiptOrderNo(record.getPurchaseRawReceiptOrderNo());
                    purchaseRawReceiptVo.setReceiptOrderStatus(record.getReceiptOrderStatus());
                    purchaseRawReceiptVo.setSupplierCode(record.getSupplierCode());
                    purchaseRawReceiptVo.setSupplierName(record.getSupplierName());
                    purchaseRawReceiptVo.setDeliverCnt(record.getDeliverCnt());
                    purchaseRawReceiptVo.setDeliverTime(record.getDeliverTime());
                    purchaseRawReceiptVo.setReceiptTime(record.getReceiptTime());
                    purchaseRawReceiptVo.setReceiptUser(record.getReceiptUser());
                    purchaseRawReceiptVo.setReceiptUsername(record.getReceiptUsername());
                    purchaseRawReceiptVo.setWarehouseCode(record.getWarehouseCode());
                    purchaseRawReceiptVo.setWarehouseName(record.getWarehouseName());
                    purchaseRawReceiptVo.setPurchaseChildOrderNo(record.getPurchaseChildOrderNo());
                    purchaseRawReceiptVo.setReceiptCnt(record.getReceiptCnt());
                    purchaseRawReceiptVo.setLogistics(record.getLogistics());
                    purchaseRawReceiptVo.setTrackingNo(record.getTrackingNo());
                    purchaseRawReceiptVo.setPurchaseRawDeliverOrderNo(record.getPurchaseRawDeliverOrderNo());
                    purchaseRawReceiptVo.setRawReceiptBizType(record.getRawReceiptBizType());
                    purchaseRawReceiptVo.setSampleChildOrderNo(record.getSampleChildOrderNo());
                    purchaseRawReceiptVo.setDevelopPamphletOrderNo(record.getDevelopPamphletOrderNo());

                    return purchaseRawReceiptVo;
                }).collect(Collectors.toList());
    }

    public static PurchaseRawReceiptDetailVo rawPoToDetailVo(PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo,
                                                             List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList,
                                                             Map<String, SupplierProductComparePo> supplierProductCompareMap,
                                                             Map<String, String> skuEncodeMap) {

        final List<PurchaseRawReceiptDetailVo.PurchaseRawReceiptItemVo> purchaseRawReceiptItemList = purchaseRawReceiptOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseRawReceiptDetailVo.PurchaseRawReceiptItemVo purchaseRawReceiptItemVo = new PurchaseRawReceiptDetailVo.PurchaseRawReceiptItemVo();
                    purchaseRawReceiptItemVo.setPurchaseRawReceiptOrderItemId(po.getPurchaseRawReceiptOrderItemId());
                    purchaseRawReceiptItemVo.setVersion(po.getVersion());
                    purchaseRawReceiptItemVo.setSkuBatchCode(po.getSkuBatchCode());
                    purchaseRawReceiptItemVo.setSku(po.getSku());
                    purchaseRawReceiptItemVo.setDeliverCnt(po.getDeliverCnt());
                    purchaseRawReceiptItemVo.setReceiptCnt(po.getReceiptCnt());
                    purchaseRawReceiptItemVo.setSkuEncode(skuEncodeMap.get(po.getSku()));


                    SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(purchaseRawReceiptOrderPo.getSupplierCode() + po.getSku());
                    if (null != supplierProductComparePo) {
                        purchaseRawReceiptItemVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                    }
                    return purchaseRawReceiptItemVo;
                }).collect(Collectors.toList());

        final PurchaseRawReceiptDetailVo purchaseRawReceiptDetailVo = new PurchaseRawReceiptDetailVo();
        purchaseRawReceiptDetailVo.setPurchaseRawReceiptOrderId(purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderId());
        purchaseRawReceiptDetailVo.setVersion(purchaseRawReceiptOrderPo.getVersion());
        purchaseRawReceiptDetailVo.setPurchaseRawReceiptOrderNo(purchaseRawReceiptOrderPo.getPurchaseRawReceiptOrderNo());
        purchaseRawReceiptDetailVo.setReceiptOrderStatus(purchaseRawReceiptOrderPo.getReceiptOrderStatus());
        purchaseRawReceiptDetailVo.setWarehouseCode(purchaseRawReceiptOrderPo.getWarehouseCode());
        purchaseRawReceiptDetailVo.setWarehouseName(purchaseRawReceiptOrderPo.getWarehouseName());
        purchaseRawReceiptDetailVo.setPurchaseChildOrderNo(purchaseRawReceiptOrderPo.getPurchaseChildOrderNo());
        purchaseRawReceiptDetailVo.setLogistics(purchaseRawReceiptOrderPo.getLogistics());
        purchaseRawReceiptDetailVo.setTrackingNo(purchaseRawReceiptOrderPo.getTrackingNo());
        purchaseRawReceiptDetailVo.setSupplierCode(purchaseRawReceiptOrderPo.getSupplierCode());
        purchaseRawReceiptDetailVo.setSupplierName(purchaseRawReceiptOrderPo.getSupplierName());
        purchaseRawReceiptDetailVo.setDeliverTime(purchaseRawReceiptOrderPo.getDeliverTime());
        purchaseRawReceiptDetailVo.setPurchaseRawDeliverOrderNo(purchaseRawReceiptOrderPo.getPurchaseRawDeliverOrderNo());
        purchaseRawReceiptDetailVo.setRawReceiptBizType(purchaseRawReceiptOrderPo.getRawReceiptBizType());
        purchaseRawReceiptDetailVo.setSampleChildOrderNo(purchaseRawReceiptOrderPo.getSampleChildOrderNo());

        purchaseRawReceiptDetailVo.setPurchaseRawReceiptItemList(purchaseRawReceiptItemList);

        return purchaseRawReceiptDetailVo;
    }

    public static List<PurchaseRawReceiptOrderItemPo> rawReceiptItemDtoToPoList(List<PurchaseRawCommitDto.PurchaseRawReceiptItemDto> purchaseRawReceiptItemList) {
        return purchaseRawReceiptItemList.stream()
                .map(dto -> {
                    final PurchaseRawReceiptOrderItemPo purchaseRawReceiptOrderItemPo = new PurchaseRawReceiptOrderItemPo();
                    purchaseRawReceiptOrderItemPo.setPurchaseRawReceiptOrderItemId(dto.getPurchaseRawReceiptOrderItemId());
                    purchaseRawReceiptOrderItemPo.setVersion(dto.getVersion());
                    purchaseRawReceiptOrderItemPo.setReceiptCnt(dto.getReceiptCnt());
                    return purchaseRawReceiptOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static ReceiveOrderCreateMqDto deliverPoToMqDto(PurchaseDeliverDto dto, List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                                                           List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList,
                                                           Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap,
                                                           Map<String, List<String>> deliverNoShippingMap,
                                                           Map<String, WmsEnum.QcType> warehouseQcTypeMap) {
        final Map<String, PurchaseDeliverOrderPo> purchaseDeliverOrderNoPoMap = purchaseDeliverOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, Function.identity()));
        final Map<String, List<ReceiveOrderCreateMqDto.ReceiveOrderDetail>> receiveOrderDetailMap = purchaseDeliverOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderNoPoMap.get(po.getPurchaseDeliverOrderNo());
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
                    final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                    receiveOrderDetail.setDeliverOrderNo(po.getPurchaseDeliverOrderNo());
                    receiveOrderDetail.setBatchCode(po.getSkuBatchCode());
                    receiveOrderDetail.setSpu(po.getSpu());
                    receiveOrderDetail.setSkuCode(po.getSku());
                    receiveOrderDetail.setPurchaseAmount(po.getPurchaseCnt());
                    receiveOrderDetail.setDeliveryAmount(po.getDeliverCnt());
                    receiveOrderDetail.setPlatCode(purchaseChildOrderPo.getPlatform());
                    return receiveOrderDetail;
                }).collect(Collectors.groupingBy(ReceiveOrderCreateMqDto.ReceiveOrderDetail::getDeliverOrderNo));

        final List<ReceiveOrderCreateMqDto.ReceiveOrderCreateItem> receiveOrderCreateItemList = purchaseDeliverOrderPoList.stream()
                .map(po -> {
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(po.getPurchaseChildOrderNo());
                    final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
                    receiveOrderCreateItem.setReceiveType(ReceiveType.BULK);
                    receiveOrderCreateItem.setShippingMarkNo(dto.getShippingMarkNo());
                    receiveOrderCreateItem.setPurchaseChildOrderNo(po.getPurchaseChildOrderNo());
                    receiveOrderCreateItem.setTrackingNumber(dto.getTrackingNo());
                    receiveOrderCreateItem.setScmBizNo(po.getPurchaseDeliverOrderNo());
                    receiveOrderCreateItem.setWarehouseCode(po.getWarehouseCode());
                    receiveOrderCreateItem.setSupplierCode(po.getSupplierCode());
                    receiveOrderCreateItem.setSupplierName(po.getSupplierName());
                    receiveOrderCreateItem.setPurchaseOrderType(purchaseChildOrderPo.getPurchaseOrderType());
                    receiveOrderCreateItem.setIsUrgentOrder(purchaseChildOrderPo.getIsUrgentOrder());
                    receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
                    receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
                    receiveOrderCreateItem.setQcType(warehouseQcTypeMap.get(po.getWarehouseCode()));
                    receiveOrderCreateItem.setIsDirectSend(po.getIsDirectSend());
                    receiveOrderCreateItem.setSendTime(LocalDateTime.now());
                    receiveOrderCreateItem.setShippingMarkNumList(deliverNoShippingMap.get(po.getPurchaseDeliverOrderNo()));
                    receiveOrderCreateItem.setUnionKey(po.getPurchaseDeliverOrderNo() + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.BULK.name());

                    final List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> receiveOrderDetailList = receiveOrderDetailMap.get(po.getPurchaseDeliverOrderNo());
                    receiveOrderCreateItem.setDetailList(receiveOrderDetailList);
                    receiveOrderCreateItem.setWarehouseCode(po.getWarehouseCode());
                    receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
                    receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());

                    return receiveOrderCreateItem;
                }).collect(Collectors.toList());

        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(receiveOrderCreateItemList);

        return receiveOrderCreateMqDto;
    }

    public static List<RawReceiptExportVo> rawReceiptPoListToExportVoList(List<PurchaseRawReceiptOrderPo> records) {
        return records.stream()
                .map(record -> {
                    final RawReceiptExportVo rawReceiptExportVo = new RawReceiptExportVo();
                    rawReceiptExportVo.setPurchaseRawDeliverOrderNo(record.getPurchaseRawDeliverOrderNo());
                    rawReceiptExportVo.setPurchaseChildOrderNo(record.getPurchaseChildOrderNo());
                    rawReceiptExportVo.setLogistics(record.getLogistics());
                    rawReceiptExportVo.setTrackingNo(record.getTrackingNo());
                    rawReceiptExportVo.setWarehouseName(record.getWarehouseName());
                    rawReceiptExportVo.setDeliverTime(record.getDeliverTime());
                    rawReceiptExportVo.setDeliverCnt(record.getDeliverCnt());
                    rawReceiptExportVo.setReceiptCnt(record.getReceiptCnt());
                    rawReceiptExportVo.setReceiptUsername(record.getReceiptUsername());
                    return rawReceiptExportVo;
                }).collect(Collectors.toList());
    }


    public static List<PurchaseRawReceiptExportVo> rawReceiptPoListToExportVoList(Map<String, PurchaseRawReceiptOrderPo> purchaseRawReceiptMap,
                                                                                  Map<String, PurchaseChildOrderPo> purchaseOrderNoMap,
                                                                                  Map<Long, PurchaseChildOrderChangePo> purchaseChangeIdMap,
                                                                                  Map<String, PurchaseChildOrderItemPo> purchaseChildNoItemMap,
                                                                                  Map<String, String> skuEncodeMap,
                                                                                  List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList,
                                                                                  Map<String, PurchaseChildOrderRawPo> purchaseChildSkuRawMap) {
        if (CollectionUtils.isEmpty(purchaseRawReceiptOrderItemPoList)) {
            return Collections.emptyList();
        }


        return purchaseRawReceiptOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseRawReceiptOrderPo purchaseRawReceiptOrderPo = purchaseRawReceiptMap.get(po.getPurchaseRawReceiptOrderNo());
                    if (null == purchaseRawReceiptOrderPo) {
                        return null;
                    }
                    final PurchaseRawReceiptExportVo purchaseRawReceiptExportVo = new PurchaseRawReceiptExportVo();
                    final PurchaseChildOrderPo purchaseChildOrderPo = purchaseOrderNoMap.get(purchaseRawReceiptOrderPo.getPurchaseChildOrderNo());
                    if (null != purchaseChildOrderPo) {
                        final PurchaseChildOrderChangePo purchaseChildOrderChangePo = purchaseChangeIdMap.get(purchaseChildOrderPo.getPurchaseChildOrderId());
                        final PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildNoItemMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo());
                        purchaseRawReceiptExportVo.setPurchaseParentOrderNo(purchaseChildOrderPo.getPurchaseParentOrderNo());
                        purchaseRawReceiptExportVo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                        purchaseRawReceiptExportVo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
                        purchaseRawReceiptExportVo.setPurchaseOrderStatus(purchaseChildOrderPo.getPurchaseOrderStatus().getRemark());
                        final PurchaseChildOrderRawPo purchaseChildOrderRawPo = purchaseChildSkuRawMap.get(purchaseChildOrderPo.getPurchaseChildOrderNo() + po.getSku());
                        if (null != purchaseChildOrderRawPo) {
                            purchaseRawReceiptExportVo.setBomDeliveryCnt(purchaseChildOrderRawPo.getDeliveryCnt());
                        }
                        purchaseRawReceiptExportVo.setRawWarehouseName(purchaseRawReceiptOrderPo.getWarehouseName());
                        purchaseRawReceiptExportVo.setSku(purchaseChildOrderItemPo.getSku());
                        purchaseRawReceiptExportVo.setSkuEncode(skuEncodeMap.get(purchaseChildOrderItemPo.getSku()));
                        purchaseRawReceiptExportVo.setPurchaseCnt(purchaseChildOrderItemPo.getPurchaseCnt());
                        purchaseRawReceiptExportVo.setQualityGoodsCnt(purchaseChildOrderItemPo.getQualityGoodsCnt());
                        purchaseRawReceiptExportVo.setDefectiveGoodsCnt(purchaseChildOrderItemPo.getDefectiveGoodsCnt());
                        purchaseRawReceiptExportVo.setPurchaseDeliverTime(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderChangePo.getDeliverTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        purchaseRawReceiptExportVo.setPurchaseReceiptTime(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderChangePo.getReceiptTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        purchaseRawReceiptExportVo.setWarehousingTime(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderChangePo.getWarehousingTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    } else {
                        log.info("采购原料收货单:{}对应的采购单为空", po.getPurchaseRawReceiptOrderNo());
                    }
                    purchaseRawReceiptExportVo.setPurchaseRawDeliverOrderNo(purchaseRawReceiptOrderPo.getPurchaseRawDeliverOrderNo());
                    purchaseRawReceiptExportVo.setDeliverTime(ScmTimeUtil.localDateTimeToStr(purchaseRawReceiptOrderPo.getDeliverTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    purchaseRawReceiptExportVo.setRawSku(po.getSku());
                    purchaseRawReceiptExportVo.setRawSkuEncode(skuEncodeMap.get(po.getSku()));
                    purchaseRawReceiptExportVo.setDeliveryCnt(po.getDeliverCnt());
                    purchaseRawReceiptExportVo.setReceiptCnt(po.getReceiptCnt());
                    purchaseRawReceiptExportVo.setReceiptTime(ScmTimeUtil.localDateTimeToStr(purchaseRawReceiptOrderPo.getReceiptTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    return purchaseRawReceiptExportVo;
                }).collect(Collectors.toList());
    }


    public static ReceiveOrderCreateMqDto.ReceiveOrderCreateItem rawDtoToReceiptMqDto(RawReturnProductItemDto itemDto,
                                                                                      PurchaseChildOrderPo purchaseChildOrderPo,
                                                                                      long snowflakeId,
                                                                                      Map<String, WmsEnum.QcType> warehouseQcTypeMap) {
        final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
        receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_MATERIAL);
        receiveOrderCreateItem.setScmBizNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        receiveOrderCreateItem.setWarehouseCode(itemDto.getWarehouseCode());
        receiveOrderCreateItem.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        receiveOrderCreateItem.setSupplierName(purchaseChildOrderPo.getSupplierName());
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
        receiveOrderCreateItem.setSendTime(LocalDateTime.now());
        receiveOrderCreateItem.setQcType(warehouseQcTypeMap.get(itemDto.getWarehouseCode()));
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
        receiveOrderCreateItem.setUnionKey(purchaseChildOrderPo.getPurchaseChildOrderNo() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                ScmBizReceiveOrderType.OUTSOURCING_PROCESS_RAW.name() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                snowflakeId);

        final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
        receiveOrderDetail.setBatchCode(itemDto.getSkuBatchCode());
        receiveOrderDetail.setSkuCode(itemDto.getSku());
        receiveOrderDetail.setPurchaseAmount(itemDto.getReturnCnt());
        receiveOrderDetail.setDeliveryAmount(itemDto.getReturnCnt());
        receiveOrderDetail.setPlatCode(purchaseChildOrderPo.getPlatform());
        receiveOrderCreateItem.setDetailList(Collections.singletonList(receiveOrderDetail));

        return receiveOrderCreateItem;
    }

    public static List<PurchaseWaitDeliverVo> waitDeliverPoListToVoList(List<PurchaseDeliverOrderPo> poList,
                                                                        Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderNoMap,
                                                                        Map<String, String> skuEncodeMap,
                                                                        Map<String, SupplierProductComparePo> supplierProductCompareMap) {
        return Optional.ofNullable(poList)
                .orElse(new ArrayList<>())
                .stream()
                .map(record -> {
                    final PurchaseWaitDeliverVo purchaseWaitDeliverVo = new PurchaseWaitDeliverVo();
                    purchaseWaitDeliverVo.setPurchaseDeliverOrderId(record.getPurchaseDeliverOrderId());
                    purchaseWaitDeliverVo.setVersion(record.getVersion());
                    purchaseWaitDeliverVo.setPurchaseChildOrderNo(record.getPurchaseChildOrderNo());
                    purchaseWaitDeliverVo.setPurchaseDeliverOrderNo(record.getPurchaseDeliverOrderNo());
                    purchaseWaitDeliverVo.setWarehouseCode(record.getWarehouseCode());
                    purchaseWaitDeliverVo.setWarehouseName(record.getWarehouseName());
                    purchaseWaitDeliverVo.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));
                    purchaseWaitDeliverVo.setSupplierCode(record.getSupplierCode());
                    purchaseWaitDeliverVo.setSupplierName(record.getSupplierName());
                    purchaseWaitDeliverVo.setDeliverCnt(record.getDeliverCnt());
                    purchaseWaitDeliverVo.setDeliverDate(record.getDeliverDate());
                    purchaseWaitDeliverVo.setShippingMarkNo(record.getShippingMarkNo());
                    purchaseWaitDeliverVo.setCreateTime(record.getCreateTime());
                    purchaseWaitDeliverVo.setIsDirectSend(record.getIsDirectSend());

                    final List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList = purchaseDeliverOrderNoMap.get(record.getPurchaseDeliverOrderNo());
                    final List<PurchaseDeliverItemVo> purchaseDeliverItemVoList = Optional.ofNullable(purchaseDeliverOrderItemPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(itemPo -> {
                                final PurchaseDeliverItemVo purchaseDeliverItemVo = new PurchaseDeliverItemVo();
                                purchaseDeliverItemVo.setSku(itemPo.getSku());
                                purchaseDeliverItemVo.setPurchaseCnt(itemPo.getPurchaseCnt());
                                purchaseDeliverItemVo.setSkuEncode(skuEncodeMap.get(itemPo.getSku()));
                                purchaseDeliverItemVo.setSkuBatchCode(itemPo.getSkuBatchCode());
                                final SupplierProductComparePo supplierProductComparePo = supplierProductCompareMap.get(record.getSupplierCode() + itemPo.getSku());
                                if (null != supplierProductComparePo) {
                                    purchaseDeliverItemVo.setSupplierProductName(supplierProductComparePo.getSupplierProductName());
                                }
                                return purchaseDeliverItemVo;
                            }).collect(Collectors.toList());

                    purchaseWaitDeliverVo.setPurchaseDeliverItemList(purchaseDeliverItemVoList);

                    return purchaseWaitDeliverVo;
                }).collect(Collectors.toList());
    }
}
