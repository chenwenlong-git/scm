package com.hete.supply.scm.server.supplier.sample.converter;

import cn.hutool.core.date.DateTime;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderItemPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnItemDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleReturnRawDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnDetailVo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.core.holder.GlobalContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/12 16:22
 */
public class SupplierSampleConverter {

    public static SampleDeliverOrderPo deliverDtoToPo(SampleChildOrderPo sampleChildOrderPo) {
        final SampleDeliverOrderPo sampleDeliverOrderPo = new SampleDeliverOrderPo();
        sampleDeliverOrderPo.setSampleDeliverOrderStatus(SampleDeliverOrderStatus.WAIT_RECEIVED_SAMPLE);
        sampleDeliverOrderPo.setDeliverUser(GlobalContext.getUserKey());
        sampleDeliverOrderPo.setDeliverUsername(GlobalContext.getUsername());
        sampleDeliverOrderPo.setDeliverTime(new DateTime().toLocalDateTime());
        sampleDeliverOrderPo.setSupplierCode(sampleChildOrderPo.getSupplierCode());
        sampleDeliverOrderPo.setSupplierName(sampleChildOrderPo.getSupplierName());
        sampleDeliverOrderPo.setWarehouseCode(sampleChildOrderPo.getWarehouseCode());
        sampleDeliverOrderPo.setWarehouseName(sampleChildOrderPo.getWarehouseName());
        sampleDeliverOrderPo.setWarehouseTypes(sampleChildOrderPo.getWarehouseTypes());

        return sampleDeliverOrderPo;

    }

    public static SampleReceiptOrderPo deliverPoToReceiptPo(SampleDeliverOrderPo po) {

        final SampleReceiptOrderPo sampleReceiptOrderPo = new SampleReceiptOrderPo();
        sampleReceiptOrderPo.setTrackingNo(po.getTrackingNo());
        sampleReceiptOrderPo.setLogistics(po.getLogistics());
        sampleReceiptOrderPo.setTotalDeliver(po.getTotalDeliver());
        sampleReceiptOrderPo.setReceiptOrderStatus(SampleReceiptOrderStatus.WAIT_RECEIVED_SAMPLE);
        sampleReceiptOrderPo.setSupplierCode(po.getSupplierCode());
        sampleReceiptOrderPo.setSupplierName(po.getSupplierName());
        sampleReceiptOrderPo.setDeliverTime(po.getDeliverTime());
        sampleReceiptOrderPo.setSampleDeliverOrderNo(po.getSampleDeliverOrderNo());
        // todo set spu?
        return sampleReceiptOrderPo;
    }

    public static List<SampleDeliverOrderItemPo> deliverDtoToItemPoList(SampleDeliverDto dto, Map<Long, SampleChildOrderPo> sampleChildOrderPoMap, String sampleDeliverOrderNo) {
        return dto.getSampleDeliverItemList().stream()
                .map(item -> {
                    final SampleDeliverOrderItemPo sampleDeliverOrderItemPo = new SampleDeliverOrderItemPo();
                    sampleDeliverOrderItemPo.setSampleDeliverOrderNo(sampleDeliverOrderNo);
                    sampleDeliverOrderItemPo.setSpu(item.getSpu());
                    final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderPoMap.get(item.getSampleChildOrderId());
                    sampleDeliverOrderItemPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                    sampleDeliverOrderItemPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                    sampleDeliverOrderItemPo.setDeliverCnt(item.getDeliverCnt());
                    return sampleDeliverOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static List<SampleReceiptOrderItemPo> deliverItemPoListToItemPoList(List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList, String sampleReceiptOrderNo) {
        return sampleDeliverOrderItemPoList.stream()
                .map(po -> {
                    final SampleReceiptOrderItemPo sampleReceiptOrderItemPo = new SampleReceiptOrderItemPo();
                    sampleReceiptOrderItemPo.setSampleReceiptOrderNo(sampleReceiptOrderNo);
                    sampleReceiptOrderItemPo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleReceiptOrderItemPo.setSampleParentOrderNo(po.getSampleParentOrderNo());
                    sampleReceiptOrderItemPo.setSpu(po.getSpu());
                    sampleReceiptOrderItemPo.setDeliverCnt(po.getDeliverCnt());
                    return sampleReceiptOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static SampleReturnDetailVo returnPoToDetailVo(SampleReturnOrderPo sampleReturnOrderPo,
                                                          List<SampleReturnOrderItemPo> sampleReturnOrderItemPoList) {
        final SampleReturnDetailVo sampleReturnDetailVo = new SampleReturnDetailVo();
        sampleReturnDetailVo.setSampleReturnOrderId(sampleReturnOrderPo.getSampleReturnOrderId());
        sampleReturnDetailVo.setVersion(sampleReturnOrderPo.getVersion());
        sampleReturnDetailVo.setSampleReturnOrderNo(sampleReturnOrderPo.getSampleReturnOrderNo());
        sampleReturnDetailVo.setReturnOrderStatus(sampleReturnOrderPo.getReturnOrderStatus());
        sampleReturnDetailVo.setLogistics(sampleReturnOrderPo.getLogistics());
        sampleReturnDetailVo.setTrackingNo(sampleReturnOrderPo.getTrackingNo());
        sampleReturnDetailVo.setSupplierCode(sampleReturnOrderPo.getSupplierCode());
        sampleReturnDetailVo.setSupplierName(sampleReturnOrderPo.getSupplierName());
        sampleReturnDetailVo.setCreateTime(sampleReturnOrderPo.getCreateTime());

        final List<SampleReturnDetailVo.SampleReturnItem> sampleReturnItemList = sampleReturnOrderItemPoList.stream()
                .map(po -> {
                    final SampleReturnDetailVo.SampleReturnItem sampleReturnItem = new SampleReturnDetailVo.SampleReturnItem();
                    sampleReturnItem.setSampleReturnOrderItemId(po.getSampleReturnOrderItemId());
                    sampleReturnItem.setVersion(po.getVersion());
                    sampleReturnItem.setSpu(po.getSpu());
                    sampleReturnItem.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleReturnItem.setReturnOrderStatus(po.getReturnOrderStatus());
                    sampleReturnItem.setReturnCnt(po.getReturnCnt());
                    sampleReturnItem.setReceiptCnt(po.getReceiptCnt());
                    return sampleReturnItem;
                }).collect(Collectors.toList());
        sampleReturnDetailVo.setSampleReturnItemList(sampleReturnItemList);

        return sampleReturnDetailVo;
    }

    public static List<SampleReturnOrderItemPo> returnConfirmDtoListToPo(List<SampleReturnItemDto> sampleReturnItemList) {
        return sampleReturnItemList.stream()
                .map(dto -> {
                    final SampleReturnOrderItemPo sampleReturnOrderItemPo = new SampleReturnOrderItemPo();
                    sampleReturnOrderItemPo.setSampleReturnOrderItemId(dto.getSampleReturnOrderItemId());
                    sampleReturnOrderItemPo.setVersion(dto.getVersion());
                    sampleReturnOrderItemPo.setReturnOrderStatus(ReceiptOrderStatus.RECEIPTED);
                    sampleReturnOrderItemPo.setReceiptCnt(dto.getReceiptCnt());

                    return sampleReturnOrderItemPo;
                }).collect(Collectors.toList());
    }

    public static List<SampleDeliverSimpleVo> deliverPoListToSimpleVoList(List<SampleDeliverOrderPo> sampleDeliverOrderPoList) {
        return sampleDeliverOrderPoList.stream()
                .map(po -> {
                    final SampleDeliverSimpleVo sampleDeliverSimpleVo = new SampleDeliverSimpleVo();
                    sampleDeliverSimpleVo.setSampleDeliverOrderNo(po.getSampleDeliverOrderNo());
                    sampleDeliverSimpleVo.setSampleDeliverOrderStatus(po.getSampleDeliverOrderStatus());
                    sampleDeliverSimpleVo.setDeliverTime(po.getDeliverTime());
                    sampleDeliverSimpleVo.setTotalDeliver(po.getTotalDeliver());
                    sampleDeliverSimpleVo.setLogistics(po.getLogistics());
                    sampleDeliverSimpleVo.setTrackingNo(po.getTrackingNo());
                    sampleDeliverSimpleVo.setDeliverUsername(po.getDeliverUsername());
                    return sampleDeliverSimpleVo;
                }).collect(Collectors.toList());
    }

    public static ReceiveOrderCreateMqDto rawDtoToReceiptMqDto(SampleReturnRawDto dto,
                                                               SampleChildOrderPo sampleChildOrderPo,
                                                               long snowflakeId) {
        final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
        receiveOrderCreateItem.setReceiveType(ReceiveType.PROCESS_MATERIAL);
        receiveOrderCreateItem.setScmBizNo(sampleChildOrderPo.getSampleChildOrderNo());
        receiveOrderCreateItem.setWarehouseCode(dto.getWarehouseCode());
        receiveOrderCreateItem.setSupplierCode(sampleChildOrderPo.getSupplierCode());
        receiveOrderCreateItem.setSupplierName(sampleChildOrderPo.getSupplierName());
        receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
        receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
        receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
        receiveOrderCreateItem.setPlaceOrderTime(LocalDateTime.now());
        receiveOrderCreateItem.setSendTime(LocalDateTime.now());
        receiveOrderCreateItem.setQcType(WmsEnum.QcType.SAMPLE_CHECK);
        receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
        receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
        receiveOrderCreateItem.setUnionKey(sampleChildOrderPo.getSampleChildOrderNo() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                ScmBizReceiveOrderType.SAMPLE_RAW.name() +
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK +
                snowflakeId);


        final List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> detailList = dto.getRawProductItemList()
                .stream()
                .map(item -> {
                    final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
                    receiveOrderDetail.setBatchCode(item.getSkuBatchCode());
                    receiveOrderDetail.setSkuCode(item.getSku());
                    receiveOrderDetail.setPurchaseAmount(item.getReturnCnt());
                    receiveOrderDetail.setDeliveryAmount(item.getReturnCnt());
                    return receiveOrderDetail;
                }).collect(Collectors.toList());
        receiveOrderCreateItem.setDetailList(detailList);

        final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
        receiveOrderCreateMqDto.setReceiveOrderCreateItemList(Collections.singletonList(receiveOrderCreateItem));

        return receiveOrderCreateMqDto;
    }
}
