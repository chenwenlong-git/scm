package com.hete.supply.scm.server.scm.qc.api.consumer;

import com.hete.supply.scm.server.scm.qc.entity.dto.ReceiveUrgentMqDto;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/4/15 15:54
 */
@Component
@RocketMQMessageListener(
        topic = "topic_wms",
        selectorExpression = "tag_change_urgent_status",
        consumerGroup = "scm_change_urgent_status_consumer"
)
@RequiredArgsConstructor
@Slf4j
public class QcOrderUrgentConsumer extends BaseMqListener<ReceiveUrgentMqDto> {
    private final QcOrderBizService qcOrderBizService;

    /**
     * 更新质检单紧急状态
     *
     * @param message 收到的消息，包含质检单创建信息。
     */
    @Override
    @SneakyThrows
    public void handleMessage(ReceiveUrgentMqDto message) {
        qcOrderBizService.urgentChangeQcMessage(message);
    }

}
