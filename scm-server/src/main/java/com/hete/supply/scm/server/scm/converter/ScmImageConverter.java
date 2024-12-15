package com.hete.supply.scm.server.scm.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 图片转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface ScmImageConverter {
    ScmImageConverter INSTANCE = Mappers.getMapper(ScmImageConverter.class);

}
