package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderProcessDescDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderProcessDescPo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleOrderProcessDescConverterImpl implements DevelopSampleOrderProcessDescConverter {

    @Override
    public List<DevelopSampleOrderProcessDescPo> convert(List<DevelopSampleOrderProcessDescDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DevelopSampleOrderProcessDescPo> list = new ArrayList<DevelopSampleOrderProcessDescPo>( dtoList.size() );
        for ( DevelopSampleOrderProcessDescDto developSampleOrderProcessDescDto : dtoList ) {
            list.add( developSampleOrderProcessDescDtoToDevelopSampleOrderProcessDescPo( developSampleOrderProcessDescDto ) );
        }

        return list;
    }

    protected DevelopSampleOrderProcessDescPo developSampleOrderProcessDescDtoToDevelopSampleOrderProcessDescPo(DevelopSampleOrderProcessDescDto developSampleOrderProcessDescDto) {
        if ( developSampleOrderProcessDescDto == null ) {
            return null;
        }

        DevelopSampleOrderProcessDescPo developSampleOrderProcessDescPo = new DevelopSampleOrderProcessDescPo();

        developSampleOrderProcessDescPo.setName( developSampleOrderProcessDescDto.getName() );
        developSampleOrderProcessDescPo.setDescValue( developSampleOrderProcessDescDto.getDescValue() );

        return developSampleOrderProcessDescPo;
    }
}
