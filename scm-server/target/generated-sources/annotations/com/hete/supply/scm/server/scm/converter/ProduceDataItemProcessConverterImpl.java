package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemProcessListVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProduceDataItemProcessConverterImpl implements ProduceDataItemProcessConverter {

    @Override
    public List<ProduceDataItemProcessListVo> convert(List<ProduceDataItemProcessPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProduceDataItemProcessListVo> list = new ArrayList<ProduceDataItemProcessListVo>( poList.size() );
        for ( ProduceDataItemProcessPo produceDataItemProcessPo : poList ) {
            list.add( produceDataItemProcessPoToProduceDataItemProcessListVo( produceDataItemProcessPo ) );
        }

        return list;
    }

    @Override
    public List<ProcessInfoVo> convertInfoVo(List<ProduceDataItemProcessPo> poList) {
        if ( poList == null ) {
            return null;
        }

        List<ProcessInfoVo> list = new ArrayList<ProcessInfoVo>( poList.size() );
        for ( ProduceDataItemProcessPo produceDataItemProcessPo : poList ) {
            list.add( produceDataItemProcessPoToProcessInfoVo( produceDataItemProcessPo ) );
        }

        return list;
    }

    protected ProduceDataItemProcessListVo produceDataItemProcessPoToProduceDataItemProcessListVo(ProduceDataItemProcessPo produceDataItemProcessPo) {
        if ( produceDataItemProcessPo == null ) {
            return null;
        }

        ProduceDataItemProcessListVo produceDataItemProcessListVo = new ProduceDataItemProcessListVo();

        produceDataItemProcessListVo.setProcessCode( produceDataItemProcessPo.getProcessCode() );
        produceDataItemProcessListVo.setProcessName( produceDataItemProcessPo.getProcessName() );
        produceDataItemProcessListVo.setProcessSecondCode( produceDataItemProcessPo.getProcessSecondCode() );
        produceDataItemProcessListVo.setProcessSecondName( produceDataItemProcessPo.getProcessSecondName() );
        produceDataItemProcessListVo.setProcessFirst( produceDataItemProcessPo.getProcessFirst() );
        produceDataItemProcessListVo.setProcessLabel( produceDataItemProcessPo.getProcessLabel() );

        return produceDataItemProcessListVo;
    }

    protected ProcessInfoVo produceDataItemProcessPoToProcessInfoVo(ProduceDataItemProcessPo produceDataItemProcessPo) {
        if ( produceDataItemProcessPo == null ) {
            return null;
        }

        ProcessInfoVo processInfoVo = new ProcessInfoVo();

        processInfoVo.setProcessCode( produceDataItemProcessPo.getProcessCode() );
        processInfoVo.setProcessName( produceDataItemProcessPo.getProcessName() );
        processInfoVo.setProcessSecondCode( produceDataItemProcessPo.getProcessSecondCode() );
        processInfoVo.setProcessSecondName( produceDataItemProcessPo.getProcessSecondName() );
        processInfoVo.setProcessFirst( produceDataItemProcessPo.getProcessFirst() );
        processInfoVo.setProcessLabel( produceDataItemProcessPo.getProcessLabel() );

        return processInfoVo;
    }
}
