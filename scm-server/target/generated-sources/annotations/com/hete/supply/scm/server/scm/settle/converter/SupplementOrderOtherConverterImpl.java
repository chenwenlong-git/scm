package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderOtherDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderOtherPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo.SupplementOrderOtherVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class SupplementOrderOtherConverterImpl implements SupplementOrderOtherConverter {

    @Override
    public List<SupplementOrderOtherPo> create(List<SupplementOrderOtherDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderOtherPo> list = new ArrayList<SupplementOrderOtherPo>( dto.size() );
        for ( SupplementOrderOtherDto supplementOrderOtherDto : dto ) {
            list.add( supplementOrderOtherDtoToSupplementOrderOtherPo( supplementOrderOtherDto ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderOtherVo> detail(List<SupplementOrderOtherPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<SupplementOrderOtherVo> list = new ArrayList<SupplementOrderOtherVo>( poList.size() );
        for ( SupplementOrderOtherPo supplementOrderOtherPo : poList ) {
            list.add( supplementOrderOtherPoToSupplementOrderOtherVo( supplementOrderOtherPo ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderOtherPo> edit(List<SupplementOrderOtherDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderOtherPo> list = new ArrayList<SupplementOrderOtherPo>( dto.size() );
        for ( SupplementOrderOtherDto supplementOrderOtherDto : dto ) {
            list.add( supplementOrderOtherDtoToSupplementOrderOtherPo( supplementOrderOtherDto ) );
        }

        return list;
    }

    protected SupplementOrderOtherPo supplementOrderOtherDtoToSupplementOrderOtherPo(SupplementOrderOtherDto supplementOrderOtherDto) {
        if ( supplementOrderOtherDto == null ) {
            return null;
        }

        SupplementOrderOtherPo supplementOrderOtherPo = new SupplementOrderOtherPo();

        supplementOrderOtherPo.setSupplementPrice( supplementOrderOtherDto.getSupplementPrice() );
        supplementOrderOtherPo.setSupplementRemarks( supplementOrderOtherDto.getSupplementRemarks() );

        return supplementOrderOtherPo;
    }

    protected SupplementOrderOtherVo supplementOrderOtherPoToSupplementOrderOtherVo(SupplementOrderOtherPo supplementOrderOtherPo) {
        if ( supplementOrderOtherPo == null ) {
            return null;
        }

        SupplementOrderOtherVo supplementOrderOtherVo = new SupplementOrderOtherVo();

        supplementOrderOtherVo.setSupplementOrderOtherId( supplementOrderOtherPo.getSupplementOrderOtherId() );
        supplementOrderOtherVo.setSupplementPrice( supplementOrderOtherPo.getSupplementPrice() );
        supplementOrderOtherVo.setSupplementRemarks( supplementOrderOtherPo.getSupplementRemarks() );

        return supplementOrderOtherVo;
    }
}
