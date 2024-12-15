package com.hete.supply.scm.server.scm.develop.api.consumer;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopParentOrderMqDto;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopChildBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * 创建开发子单
 *
 * @author ChenWenLong
 * @date 2023/8/12 09:47
 */
@Component
@RocketMQMessageListener(topic = "topic_plm", selectorExpression = "tag_create_dev_child", consumerGroup = "scm_develop_child_order_create")
@RequiredArgsConstructor
public class DevelopChildOrderCreateConsumer extends BaseMqListener<DevelopParentOrderMqDto> {
    private final DevelopChildBizService developChildBizService;

    @SneakyThrows
    @Override
    public void handleMessage(DevelopParentOrderMqDto message) {
        developChildBizService.createDevelopChildOrder(message);
    }

}
