package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderPayDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPayPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderPayVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderPayConverterImpl implements DeductOrderPayConverter {

    @Override
    public DeductOrderPayVo convert(DeductOrderPayPo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderPayVo deductOrderPayVo = new DeductOrderPayVo();

        deductOrderPayVo.setDeductOrderPayId( po.getDeductOrderPayId() );
        deductOrderPayVo.setDeductPrice( po.getDeductPrice() );
        deductOrderPayVo.setDeductRemarks( po.getDeductRemarks() );

        return deductOrderPayVo;
    }

    @Override
    public List<DeductOrderPayVo> deductOrderPayList(List<DeductOrderPayPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderPayVo> list = new ArrayList<DeductOrderPayVo>( poList.size() );
        for ( DeductOrderPayPo deductOrderPayPo : poList ) {
            list.add( convert( deductOrderPayPo ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderPayPo> create(List<DeductOrderPayDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderPayPo> list = new ArrayList<DeductOrderPayPo>( dto.size() );
        for ( DeductOrderPayDto deductOrderPayDto : dto ) {
            list.add( deductOrderPayDtoToDeductOrderPayPo( deductOrderPayDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderPayPo> edit(List<DeductOrderPayDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderPayPo> list = new ArrayList<DeductOrderPayPo>( dto.size() );
        for ( DeductOrderPayDto deductOrderPayDto : dto ) {
            list.add( deductOrderPayDtoToDeductOrderPayPo( deductOrderPayDto ) );
        }

        return list;
    }

    protected DeductOrderPayPo deductOrderPayDtoToDeductOrderPayPo(DeductOrderPayDto deductOrderPayDto) {
        if ( deductOrderPayDto == null ) {
            return null;
        }

        DeductOrderPayPo deductOrderPayPo = new DeductOrderPayPo();

        deductOrderPayPo.setDeductPrice( deductOrderPayDto.getDeductPrice() );
        deductOrderPayPo.setDeductRemarks( deductOrderPayDto.getDeductRemarks() );

        return deductOrderPayPo;
    }
}
