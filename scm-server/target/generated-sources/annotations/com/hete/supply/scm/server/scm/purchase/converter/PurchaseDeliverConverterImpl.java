package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.api.scm.entity.vo.PurchaseChildDeliverExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseDeliverExportVo;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseDeliverConverterImpl implements PurchaseDeliverConverter {

    @Override
    public PurchaseChildDeliverExportVo convert(PurchaseDeliverExportVo purchaseDeliverExportVo) {
        if ( purchaseDeliverExportVo == null ) {
            return null;
        }

        PurchaseChildDeliverExportVo purchaseChildDeliverExportVo = new PurchaseChildDeliverExportVo();

        purchaseChildDeliverExportVo.setPurchaseChildOrderNo( purchaseDeliverExportVo.getPurchaseChildOrderNo() );
        if ( purchaseDeliverExportVo.getDeliverDate() != null ) {
            purchaseChildDeliverExportVo.setDeliverDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( purchaseDeliverExportVo.getDeliverDate() ) );
        }
        purchaseChildDeliverExportVo.setPurchaseDeliverOrderNo( purchaseDeliverExportVo.getPurchaseDeliverOrderNo() );
        purchaseChildDeliverExportVo.setDeliverOrderStatus( purchaseDeliverExportVo.getDeliverOrderStatus() );
        if ( purchaseDeliverExportVo.getDeliverOrderType() != null ) {
            purchaseChildDeliverExportVo.setDeliverOrderType( purchaseDeliverExportVo.getDeliverOrderType().name() );
        }
        purchaseChildDeliverExportVo.setSupplierCode( purchaseDeliverExportVo.getSupplierCode() );
        purchaseChildDeliverExportVo.setDeliverUsername( purchaseDeliverExportVo.getDeliverUsername() );
        if ( purchaseDeliverExportVo.getDeliverTime() != null ) {
            purchaseChildDeliverExportVo.setDeliverTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( purchaseDeliverExportVo.getDeliverTime() ) );
        }
        purchaseChildDeliverExportVo.setTrackingNo( purchaseDeliverExportVo.getTrackingNo() );
        purchaseChildDeliverExportVo.setSku( purchaseDeliverExportVo.getSku() );
        purchaseChildDeliverExportVo.setSkuEncode( purchaseDeliverExportVo.getSkuEncode() );
        purchaseChildDeliverExportVo.setSkuBatchCode( purchaseDeliverExportVo.getSkuBatchCode() );
        purchaseChildDeliverExportVo.setDeliverCnt( purchaseDeliverExportVo.getDeliverCnt() );
        purchaseChildDeliverExportVo.setReceiptCnt( purchaseDeliverExportVo.getReceiptCnt() );
        purchaseChildDeliverExportVo.setQualityGoodsCnt( purchaseDeliverExportVo.getQualityGoodsCnt() );
        purchaseChildDeliverExportVo.setDefectiveGoodsCnt( purchaseDeliverExportVo.getDefectiveGoodsCnt() );
        purchaseChildDeliverExportVo.setRealityReturnCnt( purchaseDeliverExportVo.getRealityReturnCnt() );
        purchaseChildDeliverExportVo.setReceiveAmount( purchaseDeliverExportVo.getReceiveAmount() );
        if ( purchaseDeliverExportVo.getReceiptTime() != null ) {
            purchaseChildDeliverExportVo.setReceiptTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( purchaseDeliverExportVo.getReceiptTime() ) );
        }
        purchaseChildDeliverExportVo.setReceiveOrderNo( purchaseDeliverExportVo.getReceiveOrderNo() );
        purchaseChildDeliverExportVo.setReceiveOrderStateName( purchaseDeliverExportVo.getReceiveOrderStateName() );
        if ( purchaseDeliverExportVo.getWmsWarehousingTime() != null ) {
            purchaseChildDeliverExportVo.setWmsWarehousingTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( purchaseDeliverExportVo.getWmsWarehousingTime() ) );
        }

        return purchaseChildDeliverExportVo;
    }
}
