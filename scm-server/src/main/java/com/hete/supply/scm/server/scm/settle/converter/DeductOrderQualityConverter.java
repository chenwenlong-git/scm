package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderQualityDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderQualityPo;
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
public interface DeductOrderQualityConverter {
    DeductOrderQualityConverter INSTANCE = Mappers.getMapper(DeductOrderQualityConverter.class);

    DeductOrderDetailVo.DeductOrderQualityVo convert(DeductOrderQualityPo po);

    List<DeductOrderDetailVo.DeductOrderQualityVo> deductOrderQualityList(List<DeductOrderQualityPo> poList);

    List<DeductOrderQualityPo> create(List<DeductOrderQualityDto> dto);

    List<DeductOrderQualityPo> edit(List<DeductOrderQualityDto> dto);
}
