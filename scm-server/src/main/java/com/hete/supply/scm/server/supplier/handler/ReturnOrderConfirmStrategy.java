package com.hete.supply.scm.server.supplier.handler;

import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/29 09:57
 */
public interface ReturnOrderConfirmStrategy extends BaseEnumHandler<ReturnType> {
    /**
     * 确认收货后创建单据
     *
     * @param purchaseReturnOrderPo
     * @param purchaseReturnOrderItemPoList
     */
    void createAfterConfirmReturnOrder(PurchaseReturnOrderPo purchaseReturnOrderPo,
                                       List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList);

    /**
     * 设置Handler类型
     *
     * @return
     */
    @NotNull
    @Override
    ReturnType getHandlerType();
}
