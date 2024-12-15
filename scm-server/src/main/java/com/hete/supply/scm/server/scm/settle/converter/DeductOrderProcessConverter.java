package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderProcessDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderProcessPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 扣款单明细加工转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface DeductOrderProcessConverter {
    DeductOrderProcessConverter INSTANCE = Mappers.getMapper(DeductOrderProcessConverter.class);

    DeductOrderDetailVo.DeductOrderProcessVo convert(DeductOrderProcessPo po);

    List<DeductOrderDetailVo.DeductOrderProcessVo> deductOrderProcessList(List<DeductOrderProcessPo> poList);

    List<DeductOrderProcessPo> create(List<DeductOrderProcessDto> dto);

    List<DeductOrderProcessPo> edit(List<DeductOrderProcessDto> dto);
}
