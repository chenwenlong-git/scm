package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataItemDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemPo;
import com.hete.supply.scm.server.scm.production.entity.dto.ProduceDataItemInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产信息转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface ProduceDataItemConverter {
    ProduceDataItemConverter INSTANCE = Mappers.getMapper(ProduceDataItemConverter.class);

    List<ProduceDataItemPo> convert(List<ProduceDataItemDto> list);

    List<ProduceDataItemPo> itemInfoConvertPo(List<ProduceDataItemInfoDto> list);

}
