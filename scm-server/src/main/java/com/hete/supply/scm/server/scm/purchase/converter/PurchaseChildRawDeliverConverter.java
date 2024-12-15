package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawDeliverPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: weiwenxin
 * @date: 2024-03-21 22:31:42
 */
@Mapper
public interface PurchaseChildRawDeliverConverter {
    PurchaseChildRawDeliverConverter INSTANCE = Mappers.getMapper(PurchaseChildRawDeliverConverter.class);

    PurchaseChildOrderRawDeliverPo convert(PurchaseChildOrderRawDeliverPo purchaseChildOrderRawDeliverPo);
}
