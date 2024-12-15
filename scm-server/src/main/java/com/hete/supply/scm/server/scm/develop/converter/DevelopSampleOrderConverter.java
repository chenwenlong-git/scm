package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopPricingMsgVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderListVo;
import com.hete.supply.scm.server.scm.entity.vo.PricingDevelopSampleOrderListVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 开发需求样品单转换器
 *
 * @author chenwenlong
 * @date 2022 2023/08/25 09:38
 */
@Mapper
public interface DevelopSampleOrderConverter {
    DevelopSampleOrderConverter INSTANCE = Mappers.getMapper(DevelopSampleOrderConverter.class);

    List<PricingDevelopSampleOrderListVo> convert(List<DevelopSampleOrderPo> poList);

    List<DevelopPricingMsgVo.DevelopPricingMsgSampleVo> developVoList(List<DevelopSampleOrderPo> poList);

    List<DevelopSampleOrderListVo.DevelopSampleOrderItemList> convertItem(List<DevelopSampleOrderPo> poList);

}
