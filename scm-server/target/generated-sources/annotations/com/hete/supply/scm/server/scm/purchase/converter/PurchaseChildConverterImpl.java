package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.api.scm.entity.vo.PurchaseVo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseChildConverterImpl implements PurchaseChildConverter {

    @Override
    public PurchaseVo convert(PurchaseChildOrderPo po) {
        if ( po == null ) {
            return null;
        }

        PurchaseVo purchaseVo = new PurchaseVo();

        purchaseVo.setPurchaseChildOrderId( po.getPurchaseChildOrderId() );
        purchaseVo.setPurchaseChildOrderNo( po.getPurchaseChildOrderNo() );
        purchaseVo.setPurchaseParentOrderNo( po.getPurchaseParentOrderNo() );
        purchaseVo.setSampleChildOrderNo( po.getSampleChildOrderNo() );
        purchaseVo.setPurchaseOrderStatus( po.getPurchaseOrderStatus() );
        purchaseVo.setPurchaseOrderType( po.getPurchaseOrderType() );
        purchaseVo.setIsUrgentOrder( po.getIsUrgentOrder() );
        purchaseVo.setSpu( po.getSpu() );
        purchaseVo.setSkuType( po.getSkuType() );
        purchaseVo.setPlatform( po.getPlatform() );
        purchaseVo.setSupplierCode( po.getSupplierCode() );
        purchaseVo.setSupplierName( po.getSupplierName() );
        purchaseVo.setWarehouseCode( po.getWarehouseCode() );
        purchaseVo.setWarehouseName( po.getWarehouseName() );
        purchaseVo.setWarehouseTypes( po.getWarehouseTypes() );
        purchaseVo.setOrderRemarks( po.getOrderRemarks() );
        purchaseVo.setPurchaseTotal( po.getPurchaseTotal() );
        purchaseVo.setSourcePurchaseChildOrderNo( po.getSourcePurchaseChildOrderNo() );
        purchaseVo.setPromiseDate( po.getPromiseDate() );
        purchaseVo.setTimelyDeliveryCnt( po.getTimelyDeliveryCnt() );
        purchaseVo.setShippableCnt( po.getShippableCnt() );
        purchaseVo.setCapacity( po.getCapacity() );
        purchaseVo.setPlaceOrderUser( po.getPlaceOrderUser() );
        purchaseVo.setPlaceOrderUsername( po.getPlaceOrderUsername() );
        purchaseVo.setCreateTime( po.getCreateTime() );
        purchaseVo.setCreateUser( po.getCreateUser() );
        purchaseVo.setCreateUsername( po.getCreateUsername() );
        purchaseVo.setUpdateTime( po.getUpdateTime() );
        purchaseVo.setUpdateUser( po.getUpdateUser() );
        purchaseVo.setUpdateUsername( po.getUpdateUsername() );
        purchaseVo.setOrderSource( po.getOrderSource() );

        return purchaseVo;
    }
}
