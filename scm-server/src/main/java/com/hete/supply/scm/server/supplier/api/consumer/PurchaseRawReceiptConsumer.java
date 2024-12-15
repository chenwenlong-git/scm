package com.hete.supply.scm.server.supplier.api.consumer;

import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
import com.hete.supply.scm.server.scm.sample.service.base.SampleRawBaseService;
import com.hete.supply.scm.server.supplier.entity.dto.RawReceiptMqDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * wms 原料发货单签出时scm生成原料收货单
 *
 * @author weiwenxin
 * @date 2022/12/14 14:12
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_purchase_raw_sign_off", consumerGroup = "scm_purchase_raw_sign_off_consumer")
@RequiredArgsConstructor
public class PurchaseRawReceiptConsumer extends BaseMqListener<RawReceiptMqDto> {
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final SampleRawBaseService sampleRawBaseService;

    @SneakyThrows
    @Override
    public void handleMessage(RawReceiptMqDto message) {
        if (WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            purchaseRawBaseService.createRawReceiptOrder(message);
        } else if (WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            sampleRawBaseService.createRawReceiptOrder(message);
        }
    }
}
