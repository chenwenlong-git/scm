package com.hete.supply.scm.server.scm.ibfs.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowDetailVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTaskVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTimelineVo;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.mc.api.workflow.enums.WorkflowTaskStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.WorkflowMqBackStrategy;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceSettleOrderDao;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceSettleOrderPo;
import com.hete.supply.scm.server.scm.ibfs.service.biz.FinanceSettleOrderBizService;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/5/25.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementWorkflowStrategy implements WorkflowMqBackStrategy {

    private final FinanceSettleOrderDao settleOrderDao;
    private final FinanceSettleOrderBizService bizService;
    private final McRemoteService mcRemoteService;

    @Override
    public void workflowMqBackHandleBusiness(WorkflowMqCallbackDto dto,
                                             FeishuAuditOrderPo feishuAuditOrderPo) {
        log.info("结算单审批飞书回调:WorkflowMqCallbackDto{},feishuAuditOrderPo:{}",
                JSON.toJSONString(dto), JSON.toJSONString(feishuAuditOrderPo));

        // 校验飞书审批单据信息：workflowNo，taskList
        String workflowNo = dto.getWorkflowNo();
        WorkflowDetailVo workflowDetailVo = mcRemoteService.workFlowDetail(workflowNo);
        if (Objects.isNull(workflowDetailVo)) {
            log.error("结算单审批飞书回调异常!飞书审批单信息不存在，请相关同事注意！workflowNo:{}", workflowNo);
            return;
        }

        List<WorkflowTaskVo> taskList = workflowDetailVo.getTaskList();
        if (CollectionUtils.isEmpty(taskList)) {
            log.error("结算单审批飞书回调异常!审批节点taskList信息不存在，请相关同事注意！workflowNo:{}", workflowNo);
            return;
        }

        // 通过飞书业务id查询结算单信息
        Long businessId = feishuAuditOrderPo.getBusinessId();
        FinanceSettleOrderPo settleOrderPo = settleOrderDao.getById(businessId);
        if (Objects.isNull(settleOrderPo)) {
            log.error("飞书结算单：发起审批/审批中回调异常!结算单信息不存在，请相关同事注意！businessId:{}", businessId);
            return;
        }
        String settleOrderTaskId = settleOrderPo.getTaskId();

        // 飞书审批流是否完结
        boolean finishCallBack;
        final WorkflowTaskVo workflowTaskVo = taskList.stream()
                .filter(task -> task.getId()
                        .equals(settleOrderTaskId))
                .findFirst()
                .orElse(null);

        if (null != workflowTaskVo) {
            // 根据po的taskId找到的vo对象
            final int index = taskList.indexOf(workflowTaskVo);
            // 判断po之后是否还存在对象，若存在，则不是finish回调，若不存在则回调是finish
            finishCallBack = index >= taskList.size() - 1;
        } else {
            // 第一次回调时，po的taskId为空，则找不到对应的vo
            finishCallBack = false;
        }

        // 根据结算单状态处理业务逻辑：已发起/审批完成/撤回
        if (WorkflowState.FINISH.equals(workflowDetailVo.getWorkflowState()) && finishCallBack) {
            List<WorkflowTaskStatus> taskStatusList = taskList.stream()
                    .map(WorkflowTaskVo::getStatus)
                    .collect(Collectors.toList());

            if (taskStatusList.contains(WorkflowTaskStatus.REJECTED)) {
                // 飞书结算单完结：拒绝
                log.info("飞书结算单{}完结：拒绝", workflowNo);
                bizService.handleSettlementRejection(businessId, workflowTaskVo);
            } else if (taskStatusList.contains(WorkflowTaskStatus.APPROVED)) {
                // 飞书结算单完结：同意审批
                log.info("飞书结算单{}完结：同意审批", workflowNo);
                bizService.handleSettlementAgreement(businessId, workflowTaskVo);
            } else {
                throw new BizException("结算单回调审批异常！结算单审批完成但状态不属于同意/拒绝", dto.getWorkflowNo());
            }
        } else if (WorkflowState.TERMINATE.equals(workflowDetailVo.getWorkflowState())) {
            // 飞书结算单：发起人撤回
            log.info("飞书结算单{}发起人撤回", workflowNo);
            bizService.handleSettlementReversal(businessId);
        } else {
            // 飞书结算单：发起审批/审批中
            log.info("飞书结算单{}发起审批/审批中", workflowNo);

            WorkflowTaskVo nextWorkflowTask;
            if (null == workflowTaskVo) {
                nextWorkflowTask = taskList.get(0);
                final WorkflowTimelineVo workflowTimelineVo = workflowDetailVo.getTimeline()
                        .get(0);
                GlobalContext.setUserKey(workflowTimelineVo.getApproveUserCode());
                GlobalContext.setUsername(workflowTimelineVo.getApproveUsername());
            } else {
                final int index = taskList.indexOf(workflowTaskVo);
                nextWorkflowTask = taskList.get(Math.min(index + 1, taskList.size() - 1));
                GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
                GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            }
            // 若taskId相同，则无需处理
            if (nextWorkflowTask.getId()
                    .equals(settleOrderTaskId)) {
                log.info("taskId:{}相同po:{}，则无需重复处理", nextWorkflowTask.getId(), settleOrderPo);
                return;
            }
            bizService.handleSettlementInApproval(businessId, workflowNo, nextWorkflowTask);
        }
    }

    @Override
    public FeishuWorkflowType getHandlerType() {
        return FeishuWorkflowType.IBFS_SETTLEMENT_APPROVE;
    }
}
