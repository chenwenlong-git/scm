package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderProcessDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderProcessPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发需求样品单工序转换器
 *
 * @author chenwenlong
 * @date 2022 2023/08/25 09:38
 */
@Mapper
public interface DevelopSampleOrderProcessConverter {
    DevelopSampleOrderProcessConverter INSTANCE = Mappers.getMapper(DevelopSampleOrderProcessConverter.class);

    List<DevelopSampleOrderProcessPo> convert(List<DevelopSampleOrderProcessDto> dtoList);
}
