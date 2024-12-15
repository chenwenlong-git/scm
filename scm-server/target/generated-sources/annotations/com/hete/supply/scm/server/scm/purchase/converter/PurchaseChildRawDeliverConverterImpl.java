package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseChildRawDeliverConverterImpl implements PurchaseChildRawDeliverConverter {

    @Override
    public PurchaseChildOrderRawDeliverPo convert(PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo) {
        if ( purchaseChildOrderRawDeliverPo == null ) {
            return null;
        }

        PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo1 = new PurchaseChildOrderRawDeliverPo();

        purchaseChildOrderRawDeliverPo1.setCreateUser( purchaseChildOrderRawDeliverPo.getCreateUser() );
        purchaseChildOrderRawDeliverPo1.setCreateTime( purchaseChildOrderRawDeliverPo.getCreateTime() );
        purchaseChildOrderRawDeliverPo1.setUpdateUser( purchaseChildOrderRawDeliverPo.getUpdateUser() );
        purchaseChildOrderRawDeliverPo1.setUpdateTime( purchaseChildOrderRawDeliverPo.getUpdateTime() );
        purchaseChildOrderRawDeliverPo1.setVersion( purchaseChildOrderRawDeliverPo.getVersion() );
        purchaseChildOrderRawDeliverPo1.setDelTimestamp( purchaseChildOrderRawDeliverPo.getDelTimestamp() );
        purchaseChildOrderRawDeliverPo1.setPurchaseChildOrderRawDeliverId( purchaseChildOrderRawDeliverPo.getPurchaseChildOrderRawDeliverId() );
        purchaseChildOrderRawDeliverPo1.setPurchaseParentOrderNo( purchaseChildOrderRawDeliverPo.getPurchaseParentOrderNo() );
        purchaseChildOrderRawDeliverPo1.setPurchaseChildOrderNo( purchaseChildOrderRawDeliverPo.getPurchaseChildOrderNo() );
        purchaseChildOrderRawDeliverPo1.setSku( purchaseChildOrderRawDeliverPo.getSku() );
        purchaseChildOrderRawDeliverPo1.setDeliveryCnt( purchaseChildOrderRawDeliverPo.getDeliveryCnt() );
        purchaseChildOrderRawDeliverPo1.setRawSupplier( purchaseChildOrderRawDeliverPo.getRawSupplier() );
        purchaseChildOrderRawDeliverPo1.setSupplierInventoryRecordId( purchaseChildOrderRawDeliverPo.getSupplierInventoryRecordId() );
        purchaseChildOrderRawDeliverPo1.setPurchaseRawDeliverOrderNo( purchaseChildOrderRawDeliverPo.getPurchaseRawDeliverOrderNo() );
        purchaseChildOrderRawDeliverPo1.setDispenseCnt( purchaseChildOrderRawDeliverPo.getDispenseCnt() );
        purchaseChildOrderRawDeliverPo1.setParticularLocation( purchaseChildOrderRawDeliverPo.getParticularLocation() );

        return purchaseChildOrderRawDeliverPo1;
    }
}
