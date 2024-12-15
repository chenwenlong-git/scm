package com.hete.supply.scm.server.scm.settle.converter;

import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author ChenWenLong
 * @date 2023/8/4 14:31
 */
@Mapper
public interface DevelopSampleSettleOrderConverter {
    DevelopSampleSettleOrderConverter INSTANCE = Mappers.getMapper(DevelopSampleSettleOrderConverter.class);

    DevelopSampleSettleOrderDetailVo convert(DevelopSampleSettleOrderPo po);
}
