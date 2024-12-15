package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderPayDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPayPo;
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
public interface DeductOrderPayConverter {
    DeductOrderPayConverter INSTANCE = Mappers.getMapper(DeductOrderPayConverter.class);

    DeductOrderDetailVo.DeductOrderPayVo convert(DeductOrderPayPo po);

    List<DeductOrderDetailVo.DeductOrderPayVo> deductOrderPayList(List<DeductOrderPayPo> poList);

    List<DeductOrderPayPo> create(List<DeductOrderPayDto> dto);

    List<DeductOrderPayPo> edit(List<DeductOrderPayDto> dto);
}
