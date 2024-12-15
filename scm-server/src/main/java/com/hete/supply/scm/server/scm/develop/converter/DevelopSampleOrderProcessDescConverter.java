package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderProcessDescDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderProcessDescPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发需求样品单描述转换器
 *
 * @author chenwenlong
 * @date 2022 2023/08/25 09:38
 */
@Mapper
public interface DevelopSampleOrderProcessDescConverter {
    DevelopSampleOrderProcessDescConverter INSTANCE = Mappers.getMapper(DevelopSampleOrderProcessDescConverter.class);

    List<DevelopSampleOrderProcessDescPo> convert(List<DevelopSampleOrderProcessDescDto> dtoList);

}
