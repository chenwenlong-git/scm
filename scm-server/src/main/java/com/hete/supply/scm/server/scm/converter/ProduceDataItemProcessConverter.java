package com.hete.supply.scm.server.scm.converter;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataItemProcessPo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataItemProcessListVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 生产信息详情工序转换器
 *
 * @author ChenWenLong
 * @date 2023/8/15 11:46
 */
@Mapper
public interface ProduceDataItemProcessConverter {
    ProduceDataItemProcessConverter INSTANCE = Mappers.getMapper(ProduceDataItemProcessConverter.class);

    List<ProduceDataItemProcessListVo> convert(List<ProduceDataItemProcessPo> poList);

    List<ProcessInfoVo> convertInfoVo(List<ProduceDataItemProcessPo> poList);

}
