package com.hete.supply.scm.server.scm.develop.converter;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPricingOrderMechanismDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderMechanismPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderPo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/08/08 14:35
 */
@Slf4j
public class DevelopPricingOrderMechanismConverter {

    public static List<DevelopPricingOrderMechanismPo> dtoToPoList(DevelopPricingOrderPo developPricingOrderPo,
                                                                   String developSampleOrderNo,
                                                                   List<DevelopPricingOrderMechanismDto> list) {
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    DevelopPricingOrderMechanismPo developPricingOrderMechanismPo = new DevelopPricingOrderMechanismPo();
                    developPricingOrderMechanismPo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
                    developPricingOrderMechanismPo.setDevelopSampleOrderNo(developSampleOrderNo);
                    developPricingOrderMechanismPo.setHairSize(item.getHairSize());
                    developPricingOrderMechanismPo.setGramWeight(item.getGramWeight());
                    developPricingOrderMechanismPo.setPrice(item.getPrice());
                    developPricingOrderMechanismPo.setHairPrice(item.getHairPrice());
                    return developPricingOrderMechanismPo;
                }).collect(Collectors.toList());
    }

}
