package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderInfoDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderSearchVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/4 14:41
 */
@Mapper
public interface DevelopPricingOrderInfoConverter {
    DevelopPricingOrderInfoConverter INSTANCE = Mappers.getMapper(DevelopPricingOrderInfoConverter.class);

    List<PricingDevelopSampleOrderSearchVo> convert(List<DevelopPricingOrderInfoPo> poList);

    DevelopPricingOrderInfoPo convert(DevelopPricingOrderInfoDto dto);
}
