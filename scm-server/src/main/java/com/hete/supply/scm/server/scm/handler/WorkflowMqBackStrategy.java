package com.hete.supply.scm.server.scm.handler;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.support.core.handler.BaseEnumHandler;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/12/14 11:22
 */
public interface WorkflowMqBackStrategy extends BaseEnumHandler<FeishuWorkflowType> {

    /**
     * 相关业务单据逻辑处理
     *
     * @param dto:
     * @param feishuAuditOrderPo:
     * @author ChenWenLong
     * @date 2023/12/14 11:56
     */
    void workflowMqBackHandleBusiness(WorkflowMqCallbackDto dto,
                                      FeishuAuditOrderPo feishuAuditOrderPo);

    /**
     * 设置Handler类型
     */
    @NotNull
    @Override
    FeishuWorkflowType getHandlerType();
}
