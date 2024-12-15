package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplatePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailVo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T16:32:56+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
public class ProcessTemplateConverterImpl implements ProcessTemplateConverter {

    @Override
    public ProcessTemplatePo convert(ProcessTemplateCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessTemplatePo processTemplatePo = new ProcessTemplatePo();

        processTemplatePo.setName( dto.getName() );

        return processTemplatePo;
    }

    @Override
    public ProcessTemplatePo convert(ProcessTemplateEditDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessTemplatePo processTemplatePo = new ProcessTemplatePo();

        processTemplatePo.setVersion( dto.getVersion() );
        processTemplatePo.setProcessTemplateId( dto.getProcessTemplateId() );
        processTemplatePo.setName( dto.getName() );

        return processTemplatePo;
    }

    @Override
    public ProcessTemplateDetailVo convert(ProcessTemplatePo po) {
        if ( po == null ) {
            return null;
        }

        ProcessTemplateDetailVo processTemplateDetailVo = new ProcessTemplateDetailVo();

        processTemplateDetailVo.setProcessTemplateId( po.getProcessTemplateId() );
        processTemplateDetailVo.setName( po.getName() );
        processTemplateDetailVo.setProcessTemplateType( po.getProcessTemplateType() );
        processTemplateDetailVo.setTypeValue( po.getTypeValue() );
        processTemplateDetailVo.setTypeValueName( po.getTypeValueName() );
        processTemplateDetailVo.setVersion( po.getVersion() );

        return processTemplateDetailVo;
    }
}
