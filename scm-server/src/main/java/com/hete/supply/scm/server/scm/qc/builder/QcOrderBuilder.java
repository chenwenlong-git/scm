package com.hete.supply.scm.server.scm.qc.builder;

import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseCompleteQcBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseCompleteQcItemBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseQcCreateRequestBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.PurchaseQcCreateRequestItemBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/19.
 */
public class QcOrderBuilder {

    public static QcDetailPo buildPassQcDetail(QcDetailPo createPassQcDetail) {
        QcDetailPo qcDetailPo = new QcDetailPo();
        qcDetailPo.setQcOrderNo(createPassQcDetail.getQcOrderNo());
        qcDetailPo.setBatchCode(createPassQcDetail.getBatchCode());
        qcDetailPo.setSkuCode(createPassQcDetail.getSkuCode());
        qcDetailPo.setAmount(createPassQcDetail.getAmount());
        qcDetailPo.setQcResult(QcResult.PASSED);
        qcDetailPo.setPlatform(createPassQcDetail.getPlatform());
        return qcDetailPo;
    }

    public static PurchaseCompleteQcBo buildPurchaseCompleteQcBo(PurchaseChildOrderPo childOrderPo,
                                                                 QcOriginProperty qcOriginProperty,
                                                                 List<PurchaseChildOrderItemPo> childOrderItems) {
        if (Objects.isNull(childOrderPo)) {
            return null;
        }

        PurchaseCompleteQcBo purchaseCompleteQcBo = new PurchaseCompleteQcBo();
        purchaseCompleteQcBo.setPurchaseChildOrderNo(childOrderPo.getPurchaseChildOrderNo());
        purchaseCompleteQcBo.setPlatform(childOrderPo.getPlatform());
        purchaseCompleteQcBo.setQcOriginProperty(qcOriginProperty);
        purchaseCompleteQcBo.setSupplierCode(childOrderPo.getSupplierCode());
        purchaseCompleteQcBo.setWarehouseCode(childOrderPo.getWarehouseCode());

        List<PurchaseCompleteQcItemBo> purchaseCompleteQcItemBos = childOrderItems.stream()
                .map(childOrderItem -> {
                    PurchaseCompleteQcItemBo purchaseCompleteQcItemBo = new PurchaseCompleteQcItemBo();
                    purchaseCompleteQcItemBo.setSku(childOrderItem.getSku());
                    purchaseCompleteQcItemBo.setBatchCode(childOrderItem.getSkuBatchCode());
                    purchaseCompleteQcItemBo.setQcAmount(childOrderItem.getPurchaseCnt());
                    return purchaseCompleteQcItemBo;
                })
                .collect(Collectors.toList());
        purchaseCompleteQcBo.setPurchaseCompleteQcItemBos(purchaseCompleteQcItemBos);
        return purchaseCompleteQcBo;
    }

    public static PurchaseQcCreateRequestBo buildPurchaseQcCreateRequestBo(PurchaseChildOrderPo purchaseChildOrderPo,
                                                                           QcOriginProperty qcOriginProperty,
                                                                           List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos) {
        if (Objects.isNull(purchaseChildOrderPo)) {
            return null;
        }

        PurchaseQcCreateRequestBo purchaseCompleteQcBo = new PurchaseQcCreateRequestBo();
        purchaseCompleteQcBo.setPurchaseChildOrderNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
        purchaseCompleteQcBo.setPlatform(purchaseChildOrderPo.getPlatform());
        purchaseCompleteQcBo.setQcOriginProperty(qcOriginProperty);
        purchaseCompleteQcBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        purchaseCompleteQcBo.setWarehouseCode(purchaseChildOrderPo.getWarehouseCode());

        List<PurchaseQcCreateRequestItemBo> purchaseQcCreateRequestItemBos = purchaseChildOrderItemPos.stream()
                .map(childOrderItem -> {
                    PurchaseQcCreateRequestItemBo purchaseQcCreateRequestItemBo = new PurchaseQcCreateRequestItemBo();
                    purchaseQcCreateRequestItemBo.setSku(childOrderItem.getSku());
                    purchaseQcCreateRequestItemBo.setBatchCode(childOrderItem.getSkuBatchCode());
                    purchaseQcCreateRequestItemBo.setQcAmount(childOrderItem.getPurchaseCnt());
                    return purchaseQcCreateRequestItemBo;
                })
                .collect(Collectors.toList());
        purchaseCompleteQcBo.setPurchaseQcCreateRequestItemBos(purchaseQcCreateRequestItemBos);
        return purchaseCompleteQcBo;
    }
}
