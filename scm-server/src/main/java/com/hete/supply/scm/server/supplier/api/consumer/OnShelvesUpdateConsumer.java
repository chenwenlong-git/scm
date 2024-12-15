package com.hete.supply.scm.server.supplier.api.consumer;

import com.hete.supply.scm.server.scm.qc.service.biz.QcOnShelvesOrderBizService;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * wms推送给scm更新上架单信息
 *
 * @author weiwenxin
 * @date 2023/6/28 21:08
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_create_on_shelves_result", consumerGroup = "scm_create_on_shelves_result_consumer")
@RequiredArgsConstructor
public class OnShelvesUpdateConsumer extends BaseMqListener<OnShelvesOrderCreateResultMqDto> {
    private final QcOnShelvesOrderBizService onShelvesOrderService;

    @SneakyThrows
    @Override
    public void handleMessage(OnShelvesOrderCreateResultMqDto message) {
        onShelvesOrderService.handleMessage(message);
    }
}
