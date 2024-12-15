package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderProcessDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderProcessPo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleOrderProcessConverterImpl implements DevelopSampleOrderProcessConverter {

    @Override
    public List<DevelopSampleOrderProcessPo> convert(List<DevelopSampleOrderProcessDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DevelopSampleOrderProcessPo> list = new ArrayList<DevelopSampleOrderProcessPo>( dtoList.size() );
        for ( DevelopSampleOrderProcessDto developSampleOrderProcessDto : dtoList ) {
            list.add( developSampleOrderProcessDtoToDevelopSampleOrderProcessPo( developSampleOrderProcessDto ) );
        }

        return list;
    }

    protected DevelopSampleOrderProcessPo developSampleOrderProcessDtoToDevelopSampleOrderProcessPo(DevelopSampleOrderProcessDto developSampleOrderProcessDto) {
        if ( developSampleOrderProcessDto == null ) {
            return null;
        }

        DevelopSampleOrderProcessPo developSampleOrderProcessPo = new DevelopSampleOrderProcessPo();

        developSampleOrderProcessPo.setProcessCode( developSampleOrderProcessDto.getProcessCode() );
        developSampleOrderProcessPo.setProcessName( developSampleOrderProcessDto.getProcessName() );
        developSampleOrderProcessPo.setProcessSecondCode( developSampleOrderProcessDto.getProcessSecondCode() );
        developSampleOrderProcessPo.setProcessSecondName( developSampleOrderProcessDto.getProcessSecondName() );
        developSampleOrderProcessPo.setProcessFirst( developSampleOrderProcessDto.getProcessFirst() );
        developSampleOrderProcessPo.setProcessLabel( developSampleOrderProcessDto.getProcessLabel() );

        return developSampleOrderProcessPo;
    }
}
