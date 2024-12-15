package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleOrderRawDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderRawPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发需求样品单原料转换器
 *
 * @author chenwenlong
 * @date 2022 2023/08/25 09:38
 */
@Mapper
public interface DevelopSampleOrderRawConverter {
    DevelopSampleOrderRawConverter INSTANCE = Mappers.getMapper(DevelopSampleOrderRawConverter.class);

    List<DevelopSampleOrderRawPo> convert(List<DevelopSampleOrderRawDto> dtoList);

}
