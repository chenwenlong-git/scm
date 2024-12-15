package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderProcessDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderProcessPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo.SupplementOrderProcessVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class SupplementOrderProcessConverterImpl implements SupplementOrderProcessConverter {

    @Override
    public SupplementOrderProcessVo convert(SupplementOrderProcessPo po) {
        if ( po == null ) {
            return null;
        }

        SupplementOrderProcessVo supplementOrderProcessVo = new SupplementOrderProcessVo();

        supplementOrderProcessVo.setSupplementOrderProcessId( po.getSupplementOrderProcessId() );
        supplementOrderProcessVo.setProcessOrderNo( po.getProcessOrderNo() );
        supplementOrderProcessVo.setSupplementPrice( po.getSupplementPrice() );
        supplementOrderProcessVo.setSettlePrice( po.getSettlePrice() );
        supplementOrderProcessVo.setSupplementRemarks( po.getSupplementRemarks() );

        return supplementOrderProcessVo;
    }

    @Override
    public List<SupplementOrderProcessVo> detail(List<SupplementOrderProcessPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<SupplementOrderProcessVo> list = new ArrayList<SupplementOrderProcessVo>( poList.size() );
        for ( SupplementOrderProcessPo supplementOrderProcessPo : poList ) {
            list.add( convert( supplementOrderProcessPo ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderProcessPo> create(List<SupplementOrderProcessDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderProcessPo> list = new ArrayList<SupplementOrderProcessPo>( dto.size() );
        for ( SupplementOrderProcessDto supplementOrderProcessDto : dto ) {
            list.add( supplementOrderProcessDtoToSupplementOrderProcessPo( supplementOrderProcessDto ) );
        }

        return list;
    }

    @Override
    public List<SupplementOrderProcessPo> edit(List<SupplementOrderProcessDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<SupplementOrderProcessPo> list = new ArrayList<SupplementOrderProcessPo>( dto.size() );
        for ( SupplementOrderProcessDto supplementOrderProcessDto : dto ) {
            list.add( supplementOrderProcessDtoToSupplementOrderProcessPo( supplementOrderProcessDto ) );
        }

        return list;
    }

    protected SupplementOrderProcessPo supplementOrderProcessDtoToSupplementOrderProcessPo(SupplementOrderProcessDto supplementOrderProcessDto) {
        if ( supplementOrderProcessDto == null ) {
            return null;
        }

        SupplementOrderProcessPo supplementOrderProcessPo = new SupplementOrderProcessPo();

        supplementOrderProcessPo.setProcessOrderNo( supplementOrderProcessDto.getProcessOrderNo() );
        supplementOrderProcessPo.setSupplementPrice( supplementOrderProcessDto.getSupplementPrice() );
        supplementOrderProcessPo.setSupplementRemarks( supplementOrderProcessDto.getSupplementRemarks() );

        return supplementOrderProcessPo;
    }
}
