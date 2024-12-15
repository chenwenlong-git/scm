package com.hete.supply.scm.server.scm.develop.converter;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 开发转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface DevelopConverter {
    DevelopConverter INSTANCE = Mappers.getMapper(DevelopConverter.class);


}
