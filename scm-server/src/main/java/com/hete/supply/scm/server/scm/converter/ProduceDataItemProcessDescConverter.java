package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemProcessDescPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemProcessDescListVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产信息详情描述转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface ProduceDataItemProcessDescConverter {
    ProduceDataItemProcessDescConverter INSTANCE = Mappers.getMapper(ProduceDataItemProcessDescConverter.class);

    List<ProduceDataItemProcessDescListVo> convert(List<ProduceDataItemProcessDescPo> poList);

    List<ProcessDescInfoVo> convertInfoVo(List<ProduceDataItemProcessDescPo> poList);

}
