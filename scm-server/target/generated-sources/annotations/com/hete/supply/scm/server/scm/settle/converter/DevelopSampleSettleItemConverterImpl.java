package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderItemVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleItemPo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleSettleItemConverterImpl implements DevelopSampleSettleItemConverter {

    @Override
    public List<DevelopSampleSettleOrderItemVo> convert(List<DevelopSampleSettleItemPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DevelopSampleSettleOrderItemVo> list = new ArrayList<DevelopSampleSettleOrderItemVo>( poList.size() );
        for ( DevelopSampleSettleItemPo developSampleSettleItemPo : poList ) {
            list.add( developSampleSettleItemPoToDevelopSampleSettleOrderItemVo( developSampleSettleItemPo ) );
        }

        return list;
    }

    protected DevelopSampleSettleOrderItemVo developSampleSettleItemPoToDevelopSampleSettleOrderItemVo(DevelopSampleSettleItemPo developSampleSettleItemPo) {
        if ( developSampleSettleItemPo == null ) {
            return null;
        }

        DevelopSampleSettleOrderItemVo developSampleSettleOrderItemVo = new DevelopSampleSettleOrderItemVo();

        developSampleSettleOrderItemVo.setBusinessNo( developSampleSettleItemPo.getBusinessNo() );
        developSampleSettleOrderItemVo.setSettleTime( developSampleSettleItemPo.getSettleTime() );
        developSampleSettleOrderItemVo.setDevelopChildOrderNo( developSampleSettleItemPo.getDevelopChildOrderNo() );
        developSampleSettleOrderItemVo.setDevelopSampleOrderNo( developSampleSettleItemPo.getDevelopSampleOrderNo() );
        developSampleSettleOrderItemVo.setSamplePrice( developSampleSettleItemPo.getSamplePrice() );
        developSampleSettleOrderItemVo.setDevelopSampleMethod( developSampleSettleItemPo.getDevelopSampleMethod() );

        return developSampleSettleOrderItemVo;
    }
}
