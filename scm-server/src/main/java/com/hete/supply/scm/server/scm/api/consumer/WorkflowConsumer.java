package com.hete.supply.scm.server.scm.api.consumer;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.scm.server.scm.service.biz.FeishuAuditOrderBizService;
import com.hete.support.rocketmq.listener.BaseMqListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author ChenWenLong
 * @date 2023/12/8 15:03
 */
@Component
@RocketMQMessageListener(topic = "topic_commons", selectorExpression = "tag_mc_workflow_callback", consumerGroup = "scm_feishu_audit_workflow_consumer")
@RequiredArgsConstructor
@Slf4j
public class WorkflowConsumer extends BaseMqListener<WorkflowMqCallbackDto> {

    private final FeishuAuditOrderBizService feishuAuditOrderBizService;

    @Override
    public void handleMessage(WorkflowMqCallbackDto message) {
        feishuAuditOrderBizService.handleMessage(message);
    }

}
