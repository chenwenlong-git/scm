package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderPayAddDto;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPayPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo.PurchaseSettleOrderPayVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class PurchaseSettleOrderPayConverterImpl implements PurchaseSettleOrderPayConverter {

    @Override
    public List<PurchaseSettleOrderPayVo> purchaseSettleOrderPayList(List<PurchaseSettleOrderPayPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<PurchaseSettleOrderPayVo> list = new ArrayList<PurchaseSettleOrderPayVo>( poList.size() );
        for ( PurchaseSettleOrderPayPo purchaseSettleOrderPayPo : poList ) {
            list.add( purchaseSettleOrderPayPoToPurchaseSettleOrderPayVo( purchaseSettleOrderPayPo ) );
        }

        return list;
    }

    @Override
    public PurchaseSettleOrderPayPo purchaseSettleOrderPay(PurchaseSettleOrderPayAddDto po) {
        if ( po == null ) {
            return null;
        }

        PurchaseSettleOrderPayPo purchaseSettleOrderPayPo = new PurchaseSettleOrderPayPo();

        purchaseSettleOrderPayPo.setVersion( po.getVersion() );
        purchaseSettleOrderPayPo.setPurchaseSettleOrderId( po.getPurchaseSettleOrderId() );
        purchaseSettleOrderPayPo.setTransactionNo( po.getTransactionNo() );
        purchaseSettleOrderPayPo.setPayTime( po.getPayTime() );
        purchaseSettleOrderPayPo.setPayPrice( po.getPayPrice() );
        purchaseSettleOrderPayPo.setRemarks( po.getRemarks() );

        return purchaseSettleOrderPayPo;
    }

    protected PurchaseSettleOrderPayVo purchaseSettleOrderPayPoToPurchaseSettleOrderPayVo(PurchaseSettleOrderPayPo purchaseSettleOrderPayPo) {
        if ( purchaseSettleOrderPayPo == null ) {
            return null;
        }

        PurchaseSettleOrderPayVo purchaseSettleOrderPayVo = new PurchaseSettleOrderPayVo();

        purchaseSettleOrderPayVo.setPurchaseSettleOrderPayId( purchaseSettleOrderPayPo.getPurchaseSettleOrderPayId() );
        purchaseSettleOrderPayVo.setVersion( purchaseSettleOrderPayPo.getVersion() );
        purchaseSettleOrderPayVo.setTransactionNo( purchaseSettleOrderPayPo.getTransactionNo() );
        purchaseSettleOrderPayVo.setPayTime( purchaseSettleOrderPayPo.getPayTime() );
        purchaseSettleOrderPayVo.setPayPrice( purchaseSettleOrderPayPo.getPayPrice() );

        return purchaseSettleOrderPayVo;
    }
}
