package com.hete.supply.scm.server.scm.process.api.consumer;

import com.hete.supply.scm.server.scm.process.entity.dto.GoodsProcessMqDto;
import com.hete.supply.scm.server.scm.process.service.biz.GoodsProcessBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @Author: RockyHuas
 * @date: 2022/11/8 15:35
 */
@Component
@RocketMQMessageListener(topic = "topic_plm", selectorExpression = "tag_create_goods", consumerGroup = "scm_tag_create_goods_consumer")
@RequiredArgsConstructor
public class GoodsProcessCreateConsumer extends BaseMqListener<GoodsProcessMqDto> {

    private final GoodsProcessBizService goodsProcessBizService;

    @SneakyThrows
    @Override
    public void handleMessage(GoodsProcessMqDto message) {
        goodsProcessBizService.sync(message, false);
    }
}
