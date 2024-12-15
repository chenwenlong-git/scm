package com.hete.supply.scm.server.scm.develop.api.consumer;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopCompleteInfoReturnMqDto;
import com.hete.supply.scm.server.scm.develop.service.biz.DevelopChildBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * PLM处理齐备信息创建sku返回
 *
 * @author ChenWenLong
 * @date 2023/8/12 09:47
 */
@Component
@RocketMQMessageListener(topic = "topic_plm", selectorExpression = "tag_goods_bound_notice", consumerGroup = "scm_develop_complete_info_return")
@RequiredArgsConstructor
public class DevelopCompleteInfoReturnConsumer extends BaseMqListener<DevelopCompleteInfoReturnMqDto> {
    private final DevelopChildBizService developChildBizService;

    @SneakyThrows
    @Override
    public void handleMessage(DevelopCompleteInfoReturnMqDto message) {
        developChildBizService.updateDevelopCompleteInfoReturn(message);
    }

}
