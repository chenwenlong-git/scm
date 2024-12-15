package com.hete.supply.scm.server.supplier.api.consumer;

import com.hete.supply.scm.server.supplier.entity.dto.ReturnOrderEventMqDto;
import com.hete.supply.scm.server.supplier.service.biz.SupplierReturnBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * 更新退货单信息
 *
 * @author weiwenxin
 * @date 2023/6/28 11:08
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_return_order_info", consumerGroup = "scm_return_order_info_consumer")
@RequiredArgsConstructor
public class ReturnOrderUpdateConsumer extends BaseMqListener<ReturnOrderEventMqDto> {
    private final SupplierReturnBizService supplierReturnBizService;

    @SneakyThrows
    @Override
    public void handleMessage(ReturnOrderEventMqDto message) {
        supplierReturnBizService.updateReturnOrder(message);
    }
}
