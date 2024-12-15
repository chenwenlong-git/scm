package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessConverterImpl implements ProcessConverter {

    @Override
    public ProcessPo convert(ProcessCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessPo processPo = new ProcessPo();

        processPo.setProcessFirst( dto.getProcessFirst() );
        processPo.setProcessLabel( dto.getProcessLabel() );
        processPo.setProcessSecondName( dto.getProcessSecondName() );
        processPo.setCommission( dto.getCommission() );
        processPo.setExtraCommission( dto.getExtraCommission() );
        processPo.setSetupDuration( dto.getSetupDuration() );
        processPo.setComplexCoefficient( dto.getComplexCoefficient() );

        return processPo;
    }

    @Override
    public ProcessPo convert(ProcessEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessPo processPo = new ProcessPo();

        processPo.setVersion( dto.getVersion() );
        processPo.setProcessId( dto.getProcessId() );
        processPo.setProcessFirst( dto.getProcessFirst() );
        processPo.setProcessLabel( dto.getProcessLabel() );
        processPo.setProcessType( dto.getProcessType() );
        processPo.setProcessSecondName( dto.getProcessSecondName() );
        processPo.setCommission( dto.getCommission() );
        processPo.setExtraCommission( dto.getExtraCommission() );
        processPo.setSetupDuration( dto.getSetupDuration() );
        processPo.setComplexCoefficient( dto.getComplexCoefficient() );

        return processPo;
    }

    @Override
    public ProcessVo convert(ProcessPo processPo) {
        if ( processPo == null ) {
            return null;
        }

        ProcessVo processVo = new ProcessVo();

        processVo.setProcessId( processPo.getProcessId() );
        if ( processPo.getVersion() != null ) {
            processVo.setVersion( processPo.getVersion() );
        }
        processVo.setProcessLabel( processPo.getProcessLabel() );
        processVo.setProcessType( processPo.getProcessType() );
        processVo.setProcessSecondName( processPo.getProcessSecondName() );
        processVo.setProcessName( processPo.getProcessName() );
        processVo.setProcessFirst( processPo.getProcessFirst() );
        processVo.setProcessStatus( processPo.getProcessStatus() );
        processVo.setCommission( processPo.getCommission() );
        processVo.setExtraCommission( processPo.getExtraCommission() );
        processVo.setComplexCoefficient( processPo.getComplexCoefficient() );
        processVo.setUpdateUsername( processPo.getUpdateUsername() );
        processVo.setUpdateTime( processPo.getUpdateTime() );
        processVo.setProcessSecondCode( processPo.getProcessSecondCode() );
        processVo.setProcessCode( processPo.getProcessCode() );
        processVo.setSetupDuration( processPo.getSetupDuration() );

        return processVo;
    }

    @Override
    public List<ProcessVo> convert(List<ProcessPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProcessVo> list = new ArrayList<ProcessVo>( poList.size() );
        for ( ProcessPo processPo : poList ) {
            list.add( convert( processPo ) );
        }

        return list;
    }
}
