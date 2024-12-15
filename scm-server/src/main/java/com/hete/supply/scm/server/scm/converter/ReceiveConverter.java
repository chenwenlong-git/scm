package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.vo.ReceiveOrderPrintVo;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/7/10 14:17
 */
public class ReceiveConverter {
    public static ReceiveOrderPrintVo wmsVoToReceiveOrderVo(ReceiveOrderForScmVo receiveOrderForScmVo) {
        final ReceiveOrderPrintVo receiveOrderVo = new ReceiveOrderPrintVo();
        receiveOrderVo.setWarehouseCode(receiveOrderForScmVo.getWarehouseCode());
        receiveOrderVo.setWarehouseName(receiveOrderForScmVo.getWarehouseName());
        receiveOrderVo.setSupplierCode(receiveOrderForScmVo.getSupplierCode());
        receiveOrderVo.setSupplierName(receiveOrderForScmVo.getSupplierName());
        receiveOrderVo.setScmBizNo(receiveOrderForScmVo.getScmBizNo());
        receiveOrderVo.setDeliveryAmount(receiveOrderForScmVo.getDeliveryAmount());
        receiveOrderVo.setReceiveType(receiveOrderForScmVo.getReceiveType());
        receiveOrderVo.setCreateTime(receiveOrderForScmVo.getCreateTime());
        final List<ReceiveOrderForScmVo.ReceiveDeliver> wmsReceiveDeliverList = receiveOrderForScmVo.getReceiveDeliverList();
        final List<ReceiveOrderPrintVo.ReceiveDeliver> receiveDeliverList = Optional.ofNullable(wmsReceiveDeliverList)
                .orElse(Collections.emptyList())
                .stream()
                .map(wmsReceiveDeliver -> {
                    final ReceiveOrderPrintVo.ReceiveDeliver receiveDeliver = new ReceiveOrderPrintVo.ReceiveDeliver();
                    receiveDeliver.setSkuBatchCode(wmsReceiveDeliver.getBatchCode());
                    receiveDeliver.setSku(wmsReceiveDeliver.getSkuCode());
                    receiveDeliver.setDeliveryAmount(wmsReceiveDeliver.getDeliveryAmount());
                    receiveDeliver.setReceiveAmount(wmsReceiveDeliver.getReceiveAmount());
                    return receiveDeliver;
                }).collect(Collectors.toList());
        receiveOrderVo.setReceiveDeliverList(receiveDeliverList);

        return receiveOrderVo;
    }
}
