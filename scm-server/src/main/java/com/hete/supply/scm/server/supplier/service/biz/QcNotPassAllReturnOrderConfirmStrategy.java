package com.hete.supply.scm.server.supplier.service.biz;

import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.supplier.handler.ReturnOrderConfirmStrategy;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 质检整单不合格策略
 *
 * @author weiwenxin
 * @date 2023/6/29 10:37
 */
@Service
@RequiredArgsConstructor
public class QcNotPassAllReturnOrderConfirmStrategy implements ReturnOrderConfirmStrategy {

    @Override
    public void createAfterConfirmReturnOrder(PurchaseReturnOrderPo purchaseReturnOrderPo,
                                              List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPoList) {

    }

    @Override
    public ReturnType getHandlerType() {
        return ReturnType.QC_NOT_PASSED_ALL;
    }
}
