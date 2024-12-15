package com.hete.supply.scm.server.scm.sample.converter;

import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderItemPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleReceiptOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderItemVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptDetailVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptOrderItemVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/14 00:51
 */
public class SampleReceiptConverter {
    public static SampleReceiptDetailVo receiptPoToDetailVo(SampleReceiptOrderPo sampleReceiptOrderPo,
                                                            List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList,
                                                            List<SampleChildOrderPo> sampleChildOrderPoList) {
        final SampleReceiptDetailVo sampleReceiptDetailVo = new SampleReceiptDetailVo();
        sampleReceiptDetailVo.setSampleReceiptOrderId(sampleReceiptOrderPo.getSampleReceiptOrderId());
        sampleReceiptDetailVo.setVersion(sampleReceiptOrderPo.getVersion());
        sampleReceiptDetailVo.setSampleReceiptOrderNo(sampleReceiptOrderPo.getSampleReceiptOrderNo());
        sampleReceiptDetailVo.setReceiptOrderStatus(sampleReceiptOrderPo.getReceiptOrderStatus());
        sampleReceiptDetailVo.setTrackingNo(sampleReceiptOrderPo.getTrackingNo());
        sampleReceiptDetailVo.setLogistics(sampleReceiptOrderPo.getLogistics());
        sampleReceiptDetailVo.setTotalDeliver(sampleReceiptOrderPo.getTotalDeliver());
        sampleReceiptDetailVo.setTotalReceipt(sampleReceiptOrderPo.getTotalReceipt());
        sampleReceiptDetailVo.setReceiptTime(sampleReceiptOrderPo.getReceiptTime());
        sampleReceiptDetailVo.setDeliverTime(sampleReceiptOrderPo.getDeliverTime());
        sampleReceiptDetailVo.setReceiptUsername(sampleReceiptOrderPo.getReceiptUsername());
        sampleReceiptDetailVo.setSampleDeliverOrderNo(sampleReceiptOrderPo.getSampleDeliverOrderNo());
        final List<SampleReceiptOrderItemVo> detailList = sampleReceiptOrderItemPoList.stream()
                .map(po -> {
                    final SampleReceiptOrderItemVo sampleReceiptOrderItem = new SampleReceiptOrderItemVo();
                    sampleReceiptOrderItem.setSampleReceiptOrderItemId(po.getSampleReceiptOrderItemId());
                    sampleReceiptOrderItem.setVersion(po.getVersion());
                    sampleReceiptOrderItem.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleReceiptOrderItem.setSpu(po.getSpu());
                    sampleReceiptOrderItem.setReceiptCnt(po.getReceiptCnt());
                    sampleReceiptOrderItem.setDeliverCnt(po.getDeliverCnt());
                    return sampleReceiptOrderItem;
                }).collect(Collectors.toList());
        final List<SampleChildOrderItemVo> sampleChildOrderItemList = sampleChildOrderPoList.stream()
                .map(po -> {
                    final SampleChildOrderItemVo sampleChildOrderItemVo = new SampleChildOrderItemVo();
                    sampleChildOrderItemVo.setSampleChildOrderId(po.getSampleChildOrderId());
                    sampleChildOrderItemVo.setVersion(po.getVersion());
                    sampleChildOrderItemVo.setSampleChildOrderNo(po.getSampleChildOrderNo());
                    sampleChildOrderItemVo.setSampleOrderStatus(po.getSampleOrderStatus());
                    sampleChildOrderItemVo.setSettlePrice(po.getSettlePrice());
                    sampleChildOrderItemVo.setSupplierCode(po.getSupplierCode());
                    sampleChildOrderItemVo.setSupplierName(po.getSupplierName());
                    sampleChildOrderItemVo.setPurchaseCnt(po.getPurchaseCnt());
                    return sampleChildOrderItemVo;
                }).collect(Collectors.toList());

        sampleReceiptDetailVo.setSampleReceiptOrderItemList(detailList);
        sampleReceiptDetailVo.setSampleChildOrderList(sampleChildOrderItemList);
        return sampleReceiptDetailVo;
    }
}
