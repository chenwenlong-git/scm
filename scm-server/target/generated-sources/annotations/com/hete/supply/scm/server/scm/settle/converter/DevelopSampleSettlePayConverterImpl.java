package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettlePayAddDto;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderPayVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleSettlePayConverterImpl implements DevelopSampleSettlePayConverter {

    @Override
    public List<DevelopSampleSettleOrderPayVo> convert(List<DevelopSampleSettlePayPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DevelopSampleSettleOrderPayVo> list = new ArrayList<DevelopSampleSettleOrderPayVo>( poList.size() );
        for ( DevelopSampleSettlePayPo developSampleSettlePayPo : poList ) {
            list.add( developSampleSettlePayPoToDevelopSampleSettleOrderPayVo( developSampleSettlePayPo ) );
        }

        return list;
    }

    @Override
    public DevelopSampleSettlePayPo addPay(DevelopSampleSettlePayAddDto po) {
        if ( po == null ) {
            return null;
        }

        DevelopSampleSettlePayPo developSampleSettlePayPo = new DevelopSampleSettlePayPo();

        developSampleSettlePayPo.setVersion( po.getVersion() );
        developSampleSettlePayPo.setTransactionNo( po.getTransactionNo() );
        developSampleSettlePayPo.setPayTime( po.getPayTime() );
        developSampleSettlePayPo.setPayPrice( po.getPayPrice() );
        developSampleSettlePayPo.setRemarks( po.getRemarks() );

        return developSampleSettlePayPo;
    }

    protected DevelopSampleSettleOrderPayVo developSampleSettlePayPoToDevelopSampleSettleOrderPayVo(DevelopSampleSettlePayPo developSampleSettlePayPo) {
        if ( developSampleSettlePayPo == null ) {
            return null;
        }

        DevelopSampleSettleOrderPayVo developSampleSettleOrderPayVo = new DevelopSampleSettleOrderPayVo();

        developSampleSettleOrderPayVo.setDevelopSampleSettlePayId( developSampleSettlePayPo.getDevelopSampleSettlePayId() );
        developSampleSettleOrderPayVo.setVersion( developSampleSettlePayPo.getVersion() );
        developSampleSettleOrderPayVo.setTransactionNo( developSampleSettlePayPo.getTransactionNo() );
        developSampleSettleOrderPayVo.setPayTime( developSampleSettlePayPo.getPayTime() );
        developSampleSettleOrderPayVo.setPayPrice( developSampleSettlePayPo.getPayPrice() );
        developSampleSettleOrderPayVo.setRemarks( developSampleSettlePayPo.getRemarks() );

        return developSampleSettleOrderPayVo;
    }
}
