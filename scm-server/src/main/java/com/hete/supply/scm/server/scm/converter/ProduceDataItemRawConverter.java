package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemRawPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemRawListVo;
import com.hete.supply.scm.server.scm.production.entity.vo.ProduceDataItemRawInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产信息的原料转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface ProduceDataItemRawConverter {
    ProduceDataItemRawConverter INSTANCE = Mappers.getMapper(ProduceDataItemRawConverter.class);

    List<ProduceDataItemRawListVo> convert(List<ProduceDataItemRawPo> poList);

    List<ProduceDataItemRawInfoVo> convertInfoVo(List<ProduceDataItemRawPo> poList);

}
