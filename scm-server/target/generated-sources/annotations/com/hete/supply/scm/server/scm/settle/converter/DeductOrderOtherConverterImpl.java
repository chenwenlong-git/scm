package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderOtherDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderOtherPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderOtherVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderOtherConverterImpl implements DeductOrderOtherConverter {

    @Override
    public DeductOrderOtherVo convert(DeductOrderOtherPo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderOtherVo deductOrderOtherVo = new DeductOrderOtherVo();

        deductOrderOtherVo.setDeductOrderOtherId( po.getDeductOrderOtherId() );
        deductOrderOtherVo.setDeductPrice( po.getDeductPrice() );
        deductOrderOtherVo.setDeductRemarks( po.getDeductRemarks() );

        return deductOrderOtherVo;
    }

    @Override
    public List<DeductOrderOtherVo> deductOrderOtherList(List<DeductOrderOtherPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderOtherVo> list = new ArrayList<DeductOrderOtherVo>( poList.size() );
        for ( DeductOrderOtherPo deductOrderOtherPo : poList ) {
            list.add( convert( deductOrderOtherPo ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderOtherPo> create(List<DeductOrderOtherDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderOtherPo> list = new ArrayList<DeductOrderOtherPo>( dto.size() );
        for ( DeductOrderOtherDto deductOrderOtherDto : dto ) {
            list.add( deductOrderOtherDtoToDeductOrderOtherPo( deductOrderOtherDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderOtherPo> edit(List<DeductOrderOtherDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderOtherPo> list = new ArrayList<DeductOrderOtherPo>( dto.size() );
        for ( DeductOrderOtherDto deductOrderOtherDto : dto ) {
            list.add( deductOrderOtherDtoToDeductOrderOtherPo( deductOrderOtherDto ) );
        }

        return list;
    }

    protected DeductOrderOtherPo deductOrderOtherDtoToDeductOrderOtherPo(DeductOrderOtherDto deductOrderOtherDto) {
        if ( deductOrderOtherDto == null ) {
            return null;
        }

        DeductOrderOtherPo deductOrderOtherPo = new DeductOrderOtherPo();

        deductOrderOtherPo.setDeductPrice( deductOrderOtherDto.getDeductPrice() );
        deductOrderOtherPo.setDeductRemarks( deductOrderOtherDto.getDeductRemarks() );

        return deductOrderOtherPo;
    }
}
