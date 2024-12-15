package com.hete.supply.scm.server.scm.purchase.api.consumer;

import com.hete.supply.scm.server.scm.purchase.entity.dto.DeliveryOrderCreateResultEventDto;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseRawBaseService;
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
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_delivery_order_create_result", consumerGroup = "scm_tag_delivery_order_create_result_consumer")
@RequiredArgsConstructor
@Slf4j
public class WmsDeliveryCreateResultConsumer extends BaseMqListener<DeliveryOrderCreateResultEventDto> {
    private final PurchaseRawBizService purchaseRawBizService;
    private final PurchaseRawBaseService purchaseRawBaseService;
    private final SampleRawBaseService sampleRawBaseService;

    @SneakyThrows
    @Override
    public void handleMessage(DeliveryOrderCreateResultEventDto message) throws Throwable {
        // 原料出库单创建时同步在scm创建原料收货单
        if (WmsEnum.DeliveryType.PURCHASE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            purchaseRawBaseService.createRawReceiptOrder(message);
        } else if (WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL.equals(message.getDeliveryType())) {
            sampleRawBaseService.createRawReceiptOrder(message);
        }

        // 采购记录出库单号到采购原料数据
        purchaseRawBizService.recordDeliveryOrderNo(message);

    }
}
