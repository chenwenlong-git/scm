package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderInfoDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderSearchVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopPricingOrderInfoConverterImpl implements DevelopPricingOrderInfoConverter {

    @Override
    public List<PricingDevelopSampleOrderSearchVo> convert(List<DevelopPricingOrderInfoPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<PricingDevelopSampleOrderSearchVo> list = new ArrayList<PricingDevelopSampleOrderSearchVo>( poList.size() );
        for ( DevelopPricingOrderInfoPo developPricingOrderInfoPo : poList ) {
            list.add( developPricingOrderInfoPoToPricingDevelopSampleOrderSearchVo( developPricingOrderInfoPo ) );
        }

        return list;
    }

    @Override
    public DevelopPricingOrderInfoPo convert(DevelopPricingOrderInfoDto dto) {
        if ( dto == null ) {
            return null;
        }

        DevelopPricingOrderInfoPo developPricingOrderInfoPo = new DevelopPricingOrderInfoPo();

        developPricingOrderInfoPo.setDevelopSampleOrderNo( dto.getDevelopSampleOrderNo() );
        developPricingOrderInfoPo.setOrdinary( dto.getOrdinary() );
        developPricingOrderInfoPo.setFrontSize( dto.getFrontSize() );
        developPricingOrderInfoPo.setHandWeavingSize( dto.getHandWeavingSize() );
        developPricingOrderInfoPo.setPrice( dto.getPrice() );
        developPricingOrderInfoPo.setGramWeight( dto.getGramWeight() );
        developPricingOrderInfoPo.setHandWeavingPrice( dto.getHandWeavingPrice() );
        developPricingOrderInfoPo.setHandHookPrice( dto.getHandHookPrice() );
        developPricingOrderInfoPo.setProductionPrice( dto.getProductionPrice() );
        developPricingOrderInfoPo.setMeshCap( dto.getMeshCap() );
        developPricingOrderInfoPo.setCurvaturePrice( dto.getCurvaturePrice() );
        developPricingOrderInfoPo.setStainPrice( dto.getStainPrice() );
        developPricingOrderInfoPo.setFactoryProfit( dto.getFactoryProfit() );
        developPricingOrderInfoPo.setCost( dto.getCost() );
        developPricingOrderInfoPo.setQuotedPrice( dto.getQuotedPrice() );
        developPricingOrderInfoPo.setSamplePrice( dto.getSamplePrice() );
        developPricingOrderInfoPo.setRemarks( dto.getRemarks() );
        developPricingOrderInfoPo.setWeightedPrice( dto.getWeightedPrice() );
        developPricingOrderInfoPo.setSecondPrice( dto.getSecondPrice() );
        developPricingOrderInfoPo.setManagePrice( dto.getManagePrice() );
        developPricingOrderInfoPo.setCostTotalPrice( dto.getCostTotalPrice() );

        return developPricingOrderInfoPo;
    }

    protected PricingDevelopSampleOrderSearchVo developPricingOrderInfoPoToPricingDevelopSampleOrderSearchVo(DevelopPricingOrderInfoPo developPricingOrderInfoPo) {
        if ( developPricingOrderInfoPo == null ) {
            return null;
        }

        PricingDevelopSampleOrderSearchVo pricingDevelopSampleOrderSearchVo = new PricingDevelopSampleOrderSearchVo();

        pricingDevelopSampleOrderSearchVo.setDevelopSampleOrderNo( developPricingOrderInfoPo.getDevelopSampleOrderNo() );
        pricingDevelopSampleOrderSearchVo.setSamplePrice( developPricingOrderInfoPo.getSamplePrice() );

        return pricingDevelopSampleOrderSearchVo;
    }
}
