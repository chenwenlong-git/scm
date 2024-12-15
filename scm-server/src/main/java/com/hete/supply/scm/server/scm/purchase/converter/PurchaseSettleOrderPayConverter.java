package com.hete.supply.scm.server.scm.purchase.converter;

import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderPayAddDto;
import com.hete.supply.scm.server.scm.settle.entity.po.PurchaseSettleOrderPayPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleOrderDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 采购结算单支付明细转换器
 *
 * @author ChenWenLong
 * @date 2022/11/8 11:58
 */
@Mapper
public interface PurchaseSettleOrderPayConverter {
    PurchaseSettleOrderPayConverter INSTANCE = Mappers.getMapper(PurchaseSettleOrderPayConverter.class);

    List<PurchaseSettleOrderDetailVo.PurchaseSettleOrderPayVo> purchaseSettleOrderPayList(List<PurchaseSettleOrderPayPo> poList);

    PurchaseSettleOrderPayPo purchaseSettleOrderPay(PurchaseSettleOrderPayAddDto po);
}
