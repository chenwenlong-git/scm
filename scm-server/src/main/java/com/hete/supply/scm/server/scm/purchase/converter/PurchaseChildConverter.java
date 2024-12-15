package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.api.scm.entity.vo.PurchaseVo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author weiwenxin
 * @date 2024/8/8 15:46
 */
@Mapper
public interface PurchaseChildConverter {
    PurchaseChildConverter INSTANCE = Mappers.getMapper(PurchaseChildConverter.class);

    PurchaseVo convert(PurchaseChildOrderPo po);

}
