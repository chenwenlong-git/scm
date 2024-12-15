package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveEditDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderDefectivePo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 扣款单表次品退供明细转换器
 *
 * @author ChenWenLong
 * @date 2022 2023/6/21 09:38
 */
@Mapper
public interface DeductOrderDefectiveConverter {
    DeductOrderDefectiveConverter INSTANCE = Mappers.getMapper(DeductOrderDefectiveConverter.class);

    List<DeductOrderDefectivePo> convert(List<DeductOrderDefectiveDto> dto);

    List<DeductOrderDefectivePo> edit(List<DeductOrderDefectiveEditDto> dto);

    List<DeductOrderDetailVo.DeductOrderDefectiveVo> deductOrderDefectiveList(List<DeductOrderDefectivePo> poList);


}
