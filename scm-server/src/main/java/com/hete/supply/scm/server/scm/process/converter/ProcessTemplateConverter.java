package com.hete.supply.scm.server.scm.process.converter;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessTemplateEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessTemplatePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessTemplateDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: RockyHuas
 * @date: 2022/11/5 09:55
 */
@Mapper
public interface ProcessTemplateConverter {

    ProcessTemplateConverter INSTANCE = Mappers.getMapper(ProcessTemplateConverter.class);

    ProcessTemplatePo convert(ProcessTemplateCreateDto dto);

    ProcessTemplatePo convert(ProcessTemplateEditDto dto);

    ProcessTemplateDetailVo convert(ProcessTemplatePo po);

}
