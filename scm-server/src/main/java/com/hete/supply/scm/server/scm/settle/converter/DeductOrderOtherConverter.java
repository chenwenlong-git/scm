package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderOtherDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderOtherPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 扣款单品质扣款明细转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface DeductOrderOtherConverter {
    DeductOrderOtherConverter INSTANCE = Mappers.getMapper(DeductOrderOtherConverter.class);

    DeductOrderDetailVo.DeductOrderOtherVo convert(DeductOrderOtherPo po);

    List<DeductOrderDetailVo.DeductOrderOtherVo> deductOrderOtherList(List<DeductOrderOtherPo> poList);

    List<DeductOrderOtherPo> create(List<DeductOrderOtherDto> dto);

    List<DeductOrderOtherPo> edit(List<DeductOrderOtherDto> dto);
}
