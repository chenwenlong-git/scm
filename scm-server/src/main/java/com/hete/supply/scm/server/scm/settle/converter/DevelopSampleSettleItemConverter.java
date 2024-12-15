package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleItemPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/4 14:41
 */
@Mapper
public interface DevelopSampleSettleItemConverter {
    DevelopSampleSettleItemConverter INSTANCE = Mappers.getMapper(DevelopSampleSettleItemConverter.class);

    List<DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderItemVo> convert(List<DevelopSampleSettleItemPo> poList);
}
