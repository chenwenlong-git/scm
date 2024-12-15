package com.hete.supply.scm.server.supplier.api.consumer;

import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.supplier.entity.dto.ReceiveOrderRejectMqDto;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.ref.PurchaseDeliverRefService;
import com.hete.supply.scm.server.supplier.service.biz.SupplierReturnBizService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.BizException;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * wms拒收推送
 *
 * @author weiwenxin
 * @date 2022/12/14 14:12
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_receive_reject", consumerGroup = "scm_receive_reject_consumer")
@RequiredArgsConstructor
public class ReceiveRejectConsumer extends BaseMqListener<ReceiveOrderRejectMqDto> {
    private final SupplierReturnBizService supplierReturnBizService;
    private final PurchaseDeliverRefService purchaseDeliverRefService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;

    @SneakyThrows
    @Override
    public void handleMessage(ReceiveOrderRejectMqDto message) {
        // 不为大货类型时，无需处理
        if (!WmsEnum.ReceiveType.BULK.equals(message.getReceiveType())) {
            return;
        }
        final PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverRefService.getPurchaseDeliverOrderByNo(message.getScmBizNo());
        if (null == purchaseDeliverOrderPo) {
            throw new BizException("发货单号:{}，找不到对应的发货单，拒收生成收货单失败！", message.getScmBizNo());
        }
        final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        if (null == purchaseChildOrderPo) {
            throw new BizException("采购子单号:{}，找不到对应的采购子单，拒收生成收货单失败！", purchaseDeliverOrderPo.getPurchaseChildOrderNo());
        }

        supplierReturnBizService.receiveReject(message, purchaseDeliverOrderPo, purchaseChildOrderPo);
    }
}
