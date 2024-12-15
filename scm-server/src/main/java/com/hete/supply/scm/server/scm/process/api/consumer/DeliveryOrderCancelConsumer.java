package com.hete.supply.scm.server.scm.process.api.consumer;

import com.hete.supply.scm.server.scm.process.entity.dto.RepairMaterialReceiptCancelMqDto;
import com.hete.supply.scm.server.scm.process.service.biz.RepairOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 返修出库单取消MQ
 *
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Component
@RocketMQMessageListener(topic = "topic_wms",
        selectorExpression = "tag_delivery_order_cancel",
        consumerGroup = "scm_tag_process_material_delivered_cancel_consumer")
@RequiredArgsConstructor
public class DeliveryOrderCancelConsumer extends BaseMqListener<RepairMaterialReceiptCancelMqDto> {

    private final RepairOrderBizService repairOrderBizService;

    @Override
    public void handleMessage(RepairMaterialReceiptCancelMqDto message) throws Throwable {
        List<String> deliveryOrderNos = message.getDeliveryOrderNoList();
        repairOrderBizService.handleRepairDeliveryCancellation(deliveryOrderNos);
    }
}
