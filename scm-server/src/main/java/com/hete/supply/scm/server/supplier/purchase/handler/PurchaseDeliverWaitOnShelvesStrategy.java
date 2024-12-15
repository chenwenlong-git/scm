package com.hete.supply.scm.server.supplier.purchase.handler;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.*;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/12/25 10:54
 */
@Service
@RequiredArgsConstructor
public class PurchaseDeliverWaitOnShelvesStrategy implements PurchaseDeliverStrategy {
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverBaseService purchaseDeliverBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final LogBaseService logBaseService;

    @Override
    public void syncWmsChangeState(PurchaseChildOrderPo purchaseChildOrderPo,
                                   WmsEnum.ReceiveOrderState receiveOrderState,
                                   PurchaseDeliverOrderPo purchaseDeliverOrderPo,
                                   List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList,
                                   ReceiveOrderChangeMqDto dto,
                                   List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPoList,
                                   PurchaseParentOrderPo purchaseParentOrderPo,
                                   PurchaseParentOrderChangePo purchaseParentOrderChangePo,
                                   PurchaseChildOrderChangePo purchaseChildOrderChangePo,
                                   List<PurchaseParentOrderItemPo> purchaseParentOrderItemPoList) {

        // 更新采购发货单
        final DeliverOrderStatus deliverOrderStatus = purchaseDeliverBaseService.changeWmsStateToDeliverStatus(purchaseDeliverOrderPo, receiveOrderState);
        if (null == deliverOrderStatus) {
            throw new BizException("错误的发货目标状态，同步目标状态失败");
        }
        purchaseDeliverOrderPo.setDeliverOrderStatus(deliverOrderStatus);
        purchaseDeliverOrderDao.updateByIdVersion(purchaseDeliverOrderPo);

        // 更新子单
        // 获取子单目标状态
        final PurchaseOrderStatus targetStatus = purchaseDeliverBaseService.getPurchaseChildStatus(purchaseChildOrderPo.getPurchaseChildOrderNo());
        final PurchaseChildOrderPo updatePo = new PurchaseChildOrderPo();
        updatePo.setPurchaseChildOrderId(purchaseChildOrderPo.getPurchaseChildOrderId());
        updatePo.setVersion(purchaseChildOrderPo.getVersion());
        updatePo.setPurchaseOrderStatus(targetStatus);

        purchaseChildOrderDao.updateByIdVersion(updatePo);
        // 更新子单日志
        logBaseService.simpleLog(LogBizModule.PURCHASE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseChildOrderPo.getPurchaseChildOrderNo(), targetStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());
        // 更新发货单日志
        logBaseService.simpleLog(LogBizModule.SUPPLIER_PURCHASE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), targetStatus.getRemark(), Collections.emptyList(),
                dto.getOperator(), dto.getOperatorName());
    }

    @Override
    public WmsEnum.ReceiveOrderState getHandlerType() {
        return WmsEnum.ReceiveOrderState.WAIT_ONSHELVES;
    }
}
