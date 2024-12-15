package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderProductVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 采购结算单明细转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface PurchaseSettleOrderItemConverter {
    PurchaseSettleOrderItemConverter INSTANCE = Mappers.getMapper(PurchaseSettleOrderItemConverter.class);

    List<PurchaseSettleOrderProductVo.PurchaseSettleOrderProductDetail> purchaseSettleOrderItemList(List<PurchaseSettleOrderItemPo> poList);
}
