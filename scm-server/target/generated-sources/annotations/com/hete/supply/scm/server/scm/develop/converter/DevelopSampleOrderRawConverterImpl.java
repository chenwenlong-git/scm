package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderRawDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderRawPo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class DevelopSampleOrderRawConverterImpl implements DevelopSampleOrderRawConverter {

    @Override
    public List<DevelopSampleOrderRawPo> convert(List<DevelopSampleOrderRawDto> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<DevelopSampleOrderRawPo> list = new ArrayList<DevelopSampleOrderRawPo>( dtoList.size() );
        for ( DevelopSampleOrderRawDto developSampleOrderRawDto : dtoList ) {
            list.add( developSampleOrderRawDtoToDevelopSampleOrderRawPo( developSampleOrderRawDto ) );
        }

        return list;
    }

    protected DevelopSampleOrderRawPo developSampleOrderRawDtoToDevelopSampleOrderRawPo(DevelopSampleOrderRawDto developSampleOrderRawDto) {
        if ( developSampleOrderRawDto == null ) {
            return null;
        }

        DevelopSampleOrderRawPo developSampleOrderRawPo = new DevelopSampleOrderRawPo();

        developSampleOrderRawPo.setMaterialType( developSampleOrderRawDto.getMaterialType() );
        developSampleOrderRawPo.setSku( developSampleOrderRawDto.getSku() );
        developSampleOrderRawPo.setSkuCnt( developSampleOrderRawDto.getSkuCnt() );

        return developSampleOrderRawPo;
    }
}
