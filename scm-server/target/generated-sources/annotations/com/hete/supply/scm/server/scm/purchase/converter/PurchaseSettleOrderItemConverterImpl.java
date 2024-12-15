package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo.PurchaseSettleOrderProductDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseSettleOrderItemConverterImpl implements PurchaseSettleOrderItemConverter {

    @Override
    public List<PurchaseSettleOrderProductDetail> purchaseSettleOrderItemList(List<PurchaseSettleOrderItemPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<PurchaseSettleOrderProductDetail> list = new ArrayList<PurchaseSettleOrderProductDetail>( poList.size() );
        for ( PurchaseSettleOrderItemPo purchaseSettleOrderItemPo : poList ) {
            list.add( purchaseSettleOrderItemPoToPurchaseSettleOrderProductDetail( purchaseSettleOrderItemPo ) );
        }

        return list;
    }

    protected PurchaseSettleOrderProductDetail purchaseSettleOrderItemPoToPurchaseSettleOrderProductDetail(PurchaseSettleOrderItemPo purchaseSettleOrderItemPo) {
        if ( purchaseSettleOrderItemPo == null ) {
            return null;
        }

        PurchaseSettleOrderProductDetail purchaseSettleOrderProductDetail = new PurchaseSettleOrderProductDetail();

        purchaseSettleOrderProductDetail.setPurchaseSettleItemType( purchaseSettleOrderItemPo.getPurchaseSettleItemType() );
        purchaseSettleOrderProductDetail.setPurchaseSettleOrderItemId( purchaseSettleOrderItemPo.getPurchaseSettleOrderItemId() );
        purchaseSettleOrderProductDetail.setBusinessNo( purchaseSettleOrderItemPo.getBusinessNo() );
        purchaseSettleOrderProductDetail.setSettleTime( purchaseSettleOrderItemPo.getSettleTime() );
        purchaseSettleOrderProductDetail.setSkuNum( purchaseSettleOrderItemPo.getSkuNum() );
        purchaseSettleOrderProductDetail.setSettlePrice( purchaseSettleOrderItemPo.getSettlePrice() );
        purchaseSettleOrderProductDetail.setStatusName( purchaseSettleOrderItemPo.getStatusName() );

        return purchaseSettleOrderProductDetail;
    }
}
