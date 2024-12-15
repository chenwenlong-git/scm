package com.hete.supply.scm.server.scm.process.converter;


import com.hete.supply.scm.server.scm.process.entity.dto.ProcessCreateDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessEditDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/4 10:01
 */
@Mapper
public interface ProcessConverter {

    ProcessConverter INSTANCE = Mappers.getMapper(ProcessConverter.class);

    ProcessPo convert(ProcessCreateDto dto);

    ProcessPo convert(ProcessEditDto dto);

    ProcessVo convert(ProcessPo processPo);

    List<ProcessVo> convert(List<ProcessPo> poList);

}
