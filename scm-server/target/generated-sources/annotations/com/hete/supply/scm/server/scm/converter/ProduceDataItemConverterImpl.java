package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataItemDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.production.entity.dto.ProduceDataItemInfoDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProduceDataItemConverterImpl implements ProduceDataItemConverter {

    @Override
    public List<ProduceDataItemPo> convert(List<ProduceDataItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ProduceDataItemPo> list1 = new ArrayList<ProduceDataItemPo>( list.size() );
        for ( ProduceDataItemDto produceDataItemDto : list ) {
            list1.add( produceDataItemDtoToProduceDataItemPo( produceDataItemDto ) );
        }

        return list1;
    }

    @Override
    public List<ProduceDataItemPo> itemInfoConvertPo(List<ProduceDataItemInfoDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ProduceDataItemPo> list1 = new ArrayList<ProduceDataItemPo>( list.size() );
        for ( ProduceDataItemInfoDto produceDataItemInfoDto : list ) {
            list1.add( produceDataItemInfoDtoToProduceDataItemPo( produceDataItemInfoDto ) );
        }

        return list1;
    }

    protected ProduceDataItemPo produceDataItemDtoToProduceDataItemPo(ProduceDataItemDto produceDataItemDto) {
        if ( produceDataItemDto == null ) {
            return null;
        }

        ProduceDataItemPo produceDataItemPo = new ProduceDataItemPo();

        produceDataItemPo.setVersion( produceDataItemDto.getVersion() );
        produceDataItemPo.setProduceDataItemId( produceDataItemDto.getProduceDataItemId() );
        produceDataItemPo.setBusinessNo( produceDataItemDto.getBusinessNo() );
        produceDataItemPo.setBomName( produceDataItemDto.getBomName() );

        return produceDataItemPo;
    }

    protected ProduceDataItemPo produceDataItemInfoDtoToProduceDataItemPo(ProduceDataItemInfoDto produceDataItemInfoDto) {
        if ( produceDataItemInfoDto == null ) {
            return null;
        }

        ProduceDataItemPo produceDataItemPo = new ProduceDataItemPo();

        produceDataItemPo.setVersion( produceDataItemInfoDto.getVersion() );
        produceDataItemPo.setProduceDataItemId( produceDataItemInfoDto.getProduceDataItemId() );
        produceDataItemPo.setBusinessNo( produceDataItemInfoDto.getBusinessNo() );
        produceDataItemPo.setBomName( produceDataItemInfoDto.getBomName() );

        return produceDataItemPo;
    }
}
