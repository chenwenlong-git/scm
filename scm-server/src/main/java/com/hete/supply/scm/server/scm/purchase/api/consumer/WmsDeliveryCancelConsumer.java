package com.hete.supply.scm.server.scm.purchase.api.consumer;

import com.hete.supply.scm.server.scm.purchase.entity.dto.DeliveryOrderCancelEventDto;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseRawBizService;
import com.hete.supply.scm.server.scm.sample.service.base.SampleRawBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/3/21 15:01
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_delivery_order_cancel_success", consumerGroup = "scm_tag_delivery_order_cancel_success_consumer")
@RequiredArgsConstructor
@Slf4j
public class WmsDeliveryCancelConsumer extends BaseMqListener<DeliveryOrderCancelEventDto> {
    private final PurchaseRawBizService purchaseRawBizService;
    private final SampleRawBaseService sampleRawBaseService;

    @SneakyThrows
    @Override
    public void handleMessage(DeliveryOrderCancelEventDto message) throws Throwable {
        if (WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            purchaseRawBizService.cancelRawReceiptOrder(message);
        } else if (WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            sampleRawBaseService.cancelRawReceiptOrder(message);
        }

    }
}
