package com.hete.supply.scm.server.scm.process.api.consumer;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessWaveCreateMqDto;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * wms 收货单状态变化消费
 *
 * @author RockyHuas
 * @date 2022/12/06 14:16
 */
@Component
@RocketMQMessageListener(topic = "topic_wms", selectorExpression = "tag_process_wave_create", consumerGroup = "scm_tag_process_wave_create_consumer")
@RequiredArgsConstructor
@Slf4j
public class ProcessWaveCreateConsumer extends BaseMqListener<ProcessWaveCreateMqDto> {
    private final ProcessOrderBizService processOrderBizService;

    @SneakyThrows
    @Override
    public void handleMessage(ProcessWaveCreateMqDto message) {
        log.info("WMS推送给SCM收货数据：{}", message);
        processOrderBizService.createLimitedProcessOrder(message);

    }
}
