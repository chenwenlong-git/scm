package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderProcessDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderProcessPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 补款单明细加工转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface SupplementOrderProcessConverter {
    SupplementOrderProcessConverter INSTANCE = Mappers.getMapper(SupplementOrderProcessConverter.class);

    SupplementOrderDetailVo.SupplementOrderProcessVo convert(SupplementOrderProcessPo po);

    List<SupplementOrderDetailVo.SupplementOrderProcessVo> detail(List<SupplementOrderProcessPo> poList);

    List<SupplementOrderProcessPo> create(List<SupplementOrderProcessDto> dto);

    List<SupplementOrderProcessPo> edit(List<SupplementOrderProcessDto> dto);

}
