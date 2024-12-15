package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderOtherDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderOtherPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 补款单明细采购转换器
 *
 * @author chenwenlong
 * @date 2022 2022/10/25 09:38
 */
@Mapper
public interface SupplementOrderOtherConverter {
    SupplementOrderOtherConverter INSTANCE = Mappers.getMapper(SupplementOrderOtherConverter.class);

    List<SupplementOrderOtherPo> create(List<SupplementOrderOtherDto> dto);

    List<SupplementOrderDetailVo.SupplementOrderOtherVo> detail(List<SupplementOrderOtherPo> poList);

    List<SupplementOrderOtherPo> edit(List<SupplementOrderOtherDto> dto);

}
