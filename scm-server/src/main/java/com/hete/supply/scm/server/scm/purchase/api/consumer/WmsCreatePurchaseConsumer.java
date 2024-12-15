package com.hete.supply.scm.server.scm.purchase.api.consumer;

import com.hete.supply.scm.api.scm.entity.dto.PurchaseWmsCreateDto;
import com.hete.supply.scm.server.scm.purchase.service.biz.PurchaseBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/8/8 09:25
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_create_purchase", consumerGroup = "scm_tag_create_purchase_consumer")
@RequiredArgsConstructor
@Slf4j
public class WmsCreatePurchaseConsumer extends BaseMqListener<PurchaseWmsCreateDto> {
    private final PurchaseBizService purchaseBizService;

    @SneakyThrows
    @Override
    public void handleMessage(PurchaseWmsCreateDto message) throws Throwable {
        purchaseBizService.wmsCreatePurchase(message);
    }

}
