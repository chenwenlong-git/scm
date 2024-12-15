package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrBo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataAttrVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:57+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProduceDataAttrConverterImpl implements ProduceDataAttrConverter {

    @Override
    public List<ProduceDataAttrVo> convert(List<ProduceDataAttrPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataAttrVo> list = new ArrayList<ProduceDataAttrVo>( poList.size() );
        for ( ProduceDataAttrPo produceDataAttrPo : poList ) {
            list.add( produceDataAttrPoToProduceDataAttrVo( produceDataAttrPo ) );
        }

        return list;
    }

    @Override
    public List<ProduceDataAttrPo> insertConvert(List<ProduceDataAttrDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ProduceDataAttrPo> list1 = new ArrayList<ProduceDataAttrPo>( list.size() );
        for ( ProduceDataAttrDto produceDataAttrDto : list ) {
            list1.add( produceDataAttrDtoToProduceDataAttrPo( produceDataAttrDto ) );
        }

        return list1;
    }

    @Override
    public List<ProduceDataAttrBo> convertBo(List<ProduceDataAttrPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataAttrBo> list = new ArrayList<ProduceDataAttrBo>( poList.size() );
        for ( ProduceDataAttrPo produceDataAttrPo : poList ) {
            list.add( produceDataAttrPoToProduceDataAttrBo( produceDataAttrPo ) );
        }

        return list;
    }

    protected ProduceDataAttrVo produceDataAttrPoToProduceDataAttrVo(ProduceDataAttrPo produceDataAttrPo) {
        if ( produceDataAttrPo == null ) {
            return null;
        }

        ProduceDataAttrVo produceDataAttrVo = new ProduceDataAttrVo();

        produceDataAttrVo.setAttributeNameId( produceDataAttrPo.getAttributeNameId() );
        produceDataAttrVo.setAttrName( produceDataAttrPo.getAttrName() );
        produceDataAttrVo.setAttrValue( produceDataAttrPo.getAttrValue() );

        return produceDataAttrVo;
    }

    protected ProduceDataAttrPo produceDataAttrDtoToProduceDataAttrPo(ProduceDataAttrDto produceDataAttrDto) {
        if ( produceDataAttrDto == null ) {
            return null;
        }

        ProduceDataAttrPo produceDataAttrPo = new ProduceDataAttrPo();

        produceDataAttrPo.setAttrName( produceDataAttrDto.getAttrName() );
        produceDataAttrPo.setAttrValue( produceDataAttrDto.getAttrValue() );
        produceDataAttrPo.setAttributeNameId( produceDataAttrDto.getAttributeNameId() );

        return produceDataAttrPo;
    }

    protected ProduceDataAttrBo produceDataAttrPoToProduceDataAttrBo(ProduceDataAttrPo produceDataAttrPo) {
        if ( produceDataAttrPo == null ) {
            return null;
        }

        ProduceDataAttrBo produceDataAttrBo = new ProduceDataAttrBo();

        produceDataAttrBo.setAttributeNameId( produceDataAttrPo.getAttributeNameId() );
        produceDataAttrBo.setAttrName( produceDataAttrPo.getAttrName() );
        produceDataAttrBo.setAttrValue( produceDataAttrPo.getAttrValue() );

        return produceDataAttrBo;
    }
}
