package com.hete.supply.scm.server.scm.qc.api.consumer;

import com.hete.supply.scm.server.scm.qc.entity.dto.ReceiveOrderQcOrderDto;
import com.hete.supply.scm.server.scm.qc.service.biz.QcOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author yanjiawei
 * Created on 2023/10/18.
 */
@Component
@RocketMQMessageListener(
        topic = "topic_wms",
        selectorExpression = "tag_create_qc_order",
        consumerGroup = "scm_create_qc_order_consumer"
)
@RequiredArgsConstructor
@Slf4j
public class QcOrderCreateConsumer extends BaseMqListener<ReceiveOrderQcOrderDto> {

    private final QcOrderBizService qcOrderBizService;

    /**
     * 消息处理方法，接收和处理收到的质检单创建消息。
     *
     * @param message 收到的消息，包含质检单创建信息。
     */
    @Override
    @SneakyThrows
    public void handleMessage(ReceiveOrderQcOrderDto message) {
        qcOrderBizService.handleCreateQcMessage(message);
    }
}
