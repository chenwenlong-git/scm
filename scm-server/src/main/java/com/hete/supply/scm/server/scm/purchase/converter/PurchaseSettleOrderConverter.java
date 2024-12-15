package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 采购结算单转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface PurchaseSettleOrderConverter {
    PurchaseSettleOrderConverter INSTANCE = Mappers.getMapper(PurchaseSettleOrderConverter.class);

    PurchaseSettleOrderDetailVo convert(PurchaseSettleOrderPo po);


}
