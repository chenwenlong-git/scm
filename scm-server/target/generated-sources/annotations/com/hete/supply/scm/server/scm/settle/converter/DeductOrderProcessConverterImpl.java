package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderProcessDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderProcessPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo.DeductOrderProcessVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DeductOrderProcessConverterImpl implements DeductOrderProcessConverter {

    @Override
    public DeductOrderProcessVo convert(DeductOrderProcessPo po) {
        if ( po == null ) {
            return null;
        }

        DeductOrderProcessVo deductOrderProcessVo = new DeductOrderProcessVo();

        deductOrderProcessVo.setDeductOrderProcessId( po.getDeductOrderProcessId() );
        deductOrderProcessVo.setProcessOrderNo( po.getProcessOrderNo() );
        deductOrderProcessVo.setDeductPrice( po.getDeductPrice() );
        deductOrderProcessVo.setSettlePrice( po.getSettlePrice() );
        deductOrderProcessVo.setDeductRemarks( po.getDeductRemarks() );

        return deductOrderProcessVo;
    }

    @Override
    public List<DeductOrderProcessVo> deductOrderProcessList(List<DeductOrderProcessPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<DeductOrderProcessVo> list = new ArrayList<DeductOrderProcessVo>( poList.size() );
        for ( DeductOrderProcessPo deductOrderProcessPo : poList ) {
            list.add( convert( deductOrderProcessPo ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderProcessPo> create(List<DeductOrderProcessDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderProcessPo> list = new ArrayList<DeductOrderProcessPo>( dto.size() );
        for ( DeductOrderProcessDto deductOrderProcessDto : dto ) {
            list.add( deductOrderProcessDtoToDeductOrderProcessPo( deductOrderProcessDto ) );
        }

        return list;
    }

    @Override
    public List<DeductOrderProcessPo> edit(List<DeductOrderProcessDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<DeductOrderProcessPo> list = new ArrayList<DeductOrderProcessPo>( dto.size() );
        for ( DeductOrderProcessDto deductOrderProcessDto : dto ) {
            list.add( deductOrderProcessDtoToDeductOrderProcessPo( deductOrderProcessDto ) );
        }

        return list;
    }

    protected DeductOrderProcessPo deductOrderProcessDtoToDeductOrderProcessPo(DeductOrderProcessDto deductOrderProcessDto) {
        if ( deductOrderProcessDto == null ) {
            return null;
        }

        DeductOrderProcessPo deductOrderProcessPo = new DeductOrderProcessPo();

        deductOrderProcessPo.setProcessOrderId( deductOrderProcessDto.getProcessOrderId() );
        deductOrderProcessPo.setProcessOrderNo( deductOrderProcessDto.getProcessOrderNo() );
        deductOrderProcessPo.setDeductPrice( deductOrderProcessDto.getDeductPrice() );
        deductOrderProcessPo.setDeductRemarks( deductOrderProcessDto.getDeductRemarks() );

        return deductOrderProcessPo;
    }
}
