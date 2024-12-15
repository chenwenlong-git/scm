package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrBo;
import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrDto;
import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataAttrVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产信息的生产属性转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface ProduceDataAttrConverter {
    ProduceDataAttrConverter INSTANCE = Mappers.getMapper(ProduceDataAttrConverter.class);

    List<ProduceDataAttrVo> convert(List<ProduceDataAttrPo> poList);

    List<ProduceDataAttrPo> insertConvert(List<ProduceDataAttrDto> list);

    List<ProduceDataAttrBo> convertBo(List<ProduceDataAttrPo> poList);

}
