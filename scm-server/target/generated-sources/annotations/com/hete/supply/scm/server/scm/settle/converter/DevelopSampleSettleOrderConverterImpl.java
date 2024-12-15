package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleSettleOrderConverterImpl implements DevelopSampleSettleOrderConverter {

    @Override
    public DevelopSampleSettleOrderDetailVo convert(DevelopSampleSettleOrderPo po) {
        if ( po == null ) {
            return null;
        }

        DevelopSampleSettleOrderDetailVo developSampleSettleOrderDetailVo = new DevelopSampleSettleOrderDetailVo();

        developSampleSettleOrderDetailVo.setDevelopSampleSettleOrderId( po.getDevelopSampleSettleOrderId() );
        developSampleSettleOrderDetailVo.setDevelopSampleSettleOrderNo( po.getDevelopSampleSettleOrderNo() );
        developSampleSettleOrderDetailVo.setVersion( po.getVersion() );
        developSampleSettleOrderDetailVo.setDevelopSampleSettleStatus( po.getDevelopSampleSettleStatus() );
        developSampleSettleOrderDetailVo.setSupplierCode( po.getSupplierCode() );
        developSampleSettleOrderDetailVo.setSupplierName( po.getSupplierName() );
        developSampleSettleOrderDetailVo.setTotalPrice( po.getTotalPrice() );
        developSampleSettleOrderDetailVo.setDeductPrice( po.getDeductPrice() );
        developSampleSettleOrderDetailVo.setPayPrice( po.getPayPrice() );

        return developSampleSettleOrderDetailVo;
    }
}
