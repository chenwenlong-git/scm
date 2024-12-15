package com.hete.supply.scm.server.scm.purchase.handler;

import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderRawPo;
import com.hete.supply.scm.server.scm.purchase.enums.PurchaseSplitRaw;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2024/3/22 00:39
 */
public interface PurchaseRawStrategy extends BaseEnumHandler<PurchaseSplitRaw> {

    /**
     * 处理拆单补交的采购原料
     *
     * @param newPurchaseChildOrderRawPoList
     * @param idRawPoMap
     * @param purchaseChildOrderRawPoList
     * @param purchaseChildOrderPo
     * @param newPurchaseChildOrderPo
     * @param purchaseTotal
     */
    void dealSplitPurchaseRaw(List<PurchaseChildOrderRawPo> newPurchaseChildOrderRawPoList,
                              Map<Long, PurchaseChildOrderRawPo> idRawPoMap,
                              List<PurchaseChildOrderRawPo> purchaseChildOrderRawPoList,
                              PurchaseChildOrderPo purchaseChildOrderPo,
                              PurchaseChildOrderPo newPurchaseChildOrderPo,
                              Integer purchaseTotal);

    /**
     * 设置Handler类型
     *
     * @return
     */
    @NotNull
    @Override
    PurchaseSplitRaw getHandlerType();

}
