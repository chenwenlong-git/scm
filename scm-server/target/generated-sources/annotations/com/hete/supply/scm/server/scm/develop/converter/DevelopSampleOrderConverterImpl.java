package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingMsgVo.DevelopPricingMsgSampleVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderListVo.DevelopSampleOrderItemList;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderListVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleOrderConverterImpl implements DevelopSampleOrderConverter {

    @Override
    public List<PricingDevelopSampleOrderListVo> convert(List<DevelopSampleOrderPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<PricingDevelopSampleOrderListVo> list = new ArrayList<PricingDevelopSampleOrderListVo>( poList.size() );
        for ( DevelopSampleOrderPo developSampleOrderPo : poList ) {
            list.add( developSampleOrderPoToPricingDevelopSampleOrderListVo( developSampleOrderPo ) );
        }

        return list;
    }

    @Override
    public List<DevelopPricingMsgSampleVo> developVoList(List<DevelopSampleOrderPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DevelopPricingMsgSampleVo> list = new ArrayList<DevelopPricingMsgSampleVo>( poList.size() );
        for ( DevelopSampleOrderPo developSampleOrderPo : poList ) {
            list.add( developSampleOrderPoToDevelopPricingMsgSampleVo( developSampleOrderPo ) );
        }

        return list;
    }

    @Override
    public List<DevelopSampleOrderItemList> convertItem(List<DevelopSampleOrderPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DevelopSampleOrderItemList> list = new ArrayList<DevelopSampleOrderItemList>( poList.size() );
        for ( DevelopSampleOrderPo developSampleOrderPo : poList ) {
            list.add( developSampleOrderPoToDevelopSampleOrderItemList( developSampleOrderPo ) );
        }

        return list;
    }

    protected PricingDevelopSampleOrderListVo developSampleOrderPoToPricingDevelopSampleOrderListVo(DevelopSampleOrderPo developSampleOrderPo) {
        if ( developSampleOrderPo == null ) {
            return null;
        }

        PricingDevelopSampleOrderListVo pricingDevelopSampleOrderListVo = new PricingDevelopSampleOrderListVo();

        pricingDevelopSampleOrderListVo.setDevelopSampleOrderNo( developSampleOrderPo.getDevelopSampleOrderNo() );
        pricingDevelopSampleOrderListVo.setSku( developSampleOrderPo.getSku() );
        pricingDevelopSampleOrderListVo.setSkuEncode( developSampleOrderPo.getSkuEncode() );
        pricingDevelopSampleOrderListVo.setSkuBatchCode( developSampleOrderPo.getSkuBatchCode() );

        return pricingDevelopSampleOrderListVo;
    }

    protected DevelopPricingMsgSampleVo developSampleOrderPoToDevelopPricingMsgSampleVo(DevelopSampleOrderPo developSampleOrderPo) {
        if ( developSampleOrderPo == null ) {
            return null;
        }

        DevelopPricingMsgSampleVo developPricingMsgSampleVo = new DevelopPricingMsgSampleVo();

        developPricingMsgSampleVo.setDevelopSampleOrderNo( developSampleOrderPo.getDevelopSampleOrderNo() );
        developPricingMsgSampleVo.setDevelopSampleMethod( developSampleOrderPo.getDevelopSampleMethod() );
        developPricingMsgSampleVo.setDevelopSampleStatus( developSampleOrderPo.getDevelopSampleStatus() );

        return developPricingMsgSampleVo;
    }

    protected DevelopSampleOrderItemList developSampleOrderPoToDevelopSampleOrderItemList(DevelopSampleOrderPo developSampleOrderPo) {
        if ( developSampleOrderPo == null ) {
            return null;
        }

        DevelopSampleOrderItemList developSampleOrderItemList = new DevelopSampleOrderItemList();

        developSampleOrderItemList.setDevelopSampleOrderNo( developSampleOrderPo.getDevelopSampleOrderNo() );
        developSampleOrderItemList.setDevelopSampleMethod( developSampleOrderPo.getDevelopSampleMethod() );
        developSampleOrderItemList.setDevelopSampleStatus( developSampleOrderPo.getDevelopSampleStatus() );

        return developSampleOrderItemList;
    }
}
