package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemProcessDescPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemProcessDescListVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProduceDataItemProcessDescConverterImpl implements ProduceDataItemProcessDescConverter {

    @Override
    public List<ProduceDataItemProcessDescListVo> convert(List<ProduceDataItemProcessDescPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataItemProcessDescListVo> list = new ArrayList<ProduceDataItemProcessDescListVo>( poList.size() );
        for ( ProduceDataItemProcessDescPo produceDataItemProcessDescPo : poList ) {
            list.add( produceDataItemProcessDescPoToProduceDataItemProcessDescListVo( produceDataItemProcessDescPo ) );
        }

        return list;
    }

    @Override
    public List<ProcessDescInfoVo> convertInfoVo(List<ProduceDataItemProcessDescPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProcessDescInfoVo> list = new ArrayList<ProcessDescInfoVo>( poList.size() );
        for ( ProduceDataItemProcessDescPo produceDataItemProcessDescPo : poList ) {
            list.add( produceDataItemProcessDescPoToProcessDescInfoVo( produceDataItemProcessDescPo ) );
        }

        return list;
    }

    protected ProduceDataItemProcessDescListVo produceDataItemProcessDescPoToProduceDataItemProcessDescListVo(ProduceDataItemProcessDescPo produceDataItemProcessDescPo) {
        if ( produceDataItemProcessDescPo == null ) {
            return null;
        }

        ProduceDataItemProcessDescListVo produceDataItemProcessDescListVo = new ProduceDataItemProcessDescListVo();

        produceDataItemProcessDescListVo.setName( produceDataItemProcessDescPo.getName() );
        produceDataItemProcessDescListVo.setDescValue( produceDataItemProcessDescPo.getDescValue() );

        return produceDataItemProcessDescListVo;
    }

    protected ProcessDescInfoVo produceDataItemProcessDescPoToProcessDescInfoVo(ProduceDataItemProcessDescPo produceDataItemProcessDescPo) {
        if ( produceDataItemProcessDescPo == null ) {
            return null;
        }

        ProcessDescInfoVo processDescInfoVo = new ProcessDescInfoVo();

        processDescInfoVo.setName( produceDataItemProcessDescPo.getName() );
        processDescInfoVo.setDescValue( produceDataItemProcessDescPo.getDescValue() );

        return processDescInfoVo;
    }
}
