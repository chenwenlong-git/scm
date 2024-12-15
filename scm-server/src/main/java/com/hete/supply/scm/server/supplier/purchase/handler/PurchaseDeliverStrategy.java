package com.hete.supply.scm.server.supplier.purchase.handler;

import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/12/25 10:49
 */
public interface PurchaseDeliverStrategy extends BaseEnumHandler<WmsEnum.ReceiveOrderState> {

    /**
     * 同步wms状态后的操作
     *
     * @param purchaseChildOrderPo
     * @param receiveOrderState
     * @param purchaseDeliverOrderPo
     * @param purchaseChildOrderItemPoList
     * @param dto
     * @param purchaseDeliverOrderItemPoList
     * @param purchaseParentOrderPo
     * @param purchaseParentOrderChangePo
     * @param purchaseChildOrderChangePo
     * @param purchaseParentOrderItemPoList
     */
    void syncWmsChangeState(PurchaseChildOrderPo purchaseChildOrderPo,
                            WmsEnum.ReceiveOrderState receiveOrderState,
                            PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                            ReceiveOrderChangeMqDto dto,
                            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                            PurchaseParentOrderPo purchaseParentOrderPo,
                            PurchaseParentOrderChangePo purchaseParentOrderChangePo,
                            PurchaseChildOrderChangePo purchaseChildOrderChangePo,
                            List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList);

    /**
     * 设置Handler类型
     *
     * @return
     */
    @NotNull
    @Override
    WmsEnum.ReceiveOrderState getHandlerType();
}
