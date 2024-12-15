package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseSettleOrderConverterImpl implements PurchaseSettleOrderConverter {

    @Override
    public PurchaseSettleOrderDetailVo convert(PurchaseSettleOrderPo po) {
        if ( po == null ) {
            return null;
        }

        PurchaseSettleOrderDetailVo purchaseSettleOrderDetailVo = new PurchaseSettleOrderDetailVo();

        purchaseSettleOrderDetailVo.setPurchaseSettleOrderNo( po.getPurchaseSettleOrderNo() );
        purchaseSettleOrderDetailVo.setPurchaseSettleOrderId( po.getPurchaseSettleOrderId() );
        purchaseSettleOrderDetailVo.setVersion( po.getVersion() );
        purchaseSettleOrderDetailVo.setPurchaseSettleStatus( po.getPurchaseSettleStatus() );
        purchaseSettleOrderDetailVo.setSupplierCode( po.getSupplierCode() );
        purchaseSettleOrderDetailVo.setSupplierName( po.getSupplierName() );
        purchaseSettleOrderDetailVo.setAboutSettleTime( po.getAboutSettleTime() );
        purchaseSettleOrderDetailVo.setTotalPrice( po.getTotalPrice() );
        purchaseSettleOrderDetailVo.setDeductPrice( po.getDeductPrice() );
        purchaseSettleOrderDetailVo.setPayPrice( po.getPayPrice() );
        purchaseSettleOrderDetailVo.setCreateUsername( po.getCreateUsername() );
        purchaseSettleOrderDetailVo.setCreateTime( po.getCreateTime() );
        purchaseSettleOrderDetailVo.setConfirmUser( po.getConfirmUser() );
        purchaseSettleOrderDetailVo.setConfirmUsername( po.getConfirmUsername() );
        purchaseSettleOrderDetailVo.setConfirmTime( po.getConfirmTime() );
        purchaseSettleOrderDetailVo.setExamineUser( po.getExamineUser() );
        purchaseSettleOrderDetailVo.setExamineUsername( po.getExamineUsername() );
        purchaseSettleOrderDetailVo.setExamineTime( po.getExamineTime() );
        purchaseSettleOrderDetailVo.setSettleUser( po.getSettleUser() );
        purchaseSettleOrderDetailVo.setSettleUsername( po.getSettleUsername() );
        purchaseSettleOrderDetailVo.setSettleTime( po.getSettleTime() );
        purchaseSettleOrderDetailVo.setPayUser( po.getPayUser() );
        purchaseSettleOrderDetailVo.setPayUsername( po.getPayUsername() );
        purchaseSettleOrderDetailVo.setPayTime( po.getPayTime() );

        return purchaseSettleOrderDetailVo;
    }
}
