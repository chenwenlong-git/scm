package com.hete.supply.scm.server.supplier.service.biz;

import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.supplier.handler.ReturnOrderConfirmStrategy;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货单拒收策略
 *
 * @author weiwenxin
 * @date 2023/6/29 10:37
 */
@Service
@RequiredArgsConstructor
public class ReceiveRejectReturnOrderConfirmStrategy implements ReturnOrderConfirmStrategy {

    @Override
    public void createAfterConfirmReturnOrder(PurchaseReturnOrderPo purchaseReturnOrderPo,
                                              List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList) {
    }

    @Override
    public ReturnType getHandlerType() {
        return ReturnType.RECEIVE_REJECT;
    }
}
