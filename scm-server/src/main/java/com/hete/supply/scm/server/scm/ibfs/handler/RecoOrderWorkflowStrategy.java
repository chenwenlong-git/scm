package com.hete.supply.scm.server.scm.ibfs.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowDetailVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTaskVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTimelineVo;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.mc.api.workflow.enums.WorkflowTaskStatus;
import com.hete.supply.scm.api.scm.entity.enums.FinanceRecoOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.WorkflowMqBackStrategy;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderDao;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对账单逻辑处理策略
 *
 * @author ChenWenLong
 * @date 2024/5/26 21:45
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecoOrderWorkflowStrategy implements WorkflowMqBackStrategy {

    private final FinanceRecoOrderDao financeRecoOrderDao;
    private final McRemoteService mcRemoteService;
    private final LogBaseService logBaseService;

    @Override
    public void workflowMqBackHandleBusiness(WorkflowMqCallbackDto dto, FeishuAuditOrderPo feishuAuditOrderPo) {
        log.info("对账单飞书回调:{}", JacksonUtil.parse2Str(dto));
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getById(feishuAuditOrderPo.getBusinessId());
        if (null == financeRecoOrderPo) {
            throw new BizException("对账单:{}不存在，飞书回调数据异常", feishuAuditOrderPo.getBusinessId());
        }

        final WorkflowDetailVo workflowDetailVo = mcRemoteService.workFlowDetail(dto.getWorkflowNo());
        if (null == workflowDetailVo) {
            throw new BizException("飞书审批:{}不存在，数据错误", dto.getWorkflowNo());
        }

        financeRecoOrderPo.setWorkflowNo(dto.getWorkflowNo());
        final List<WorkflowTaskVo> taskList = workflowDetailVo.getTaskList();
        if (CollectionUtils.isEmpty(taskList)) {
            throw new BizException("飞书审批:{}不存在审批节点，数据错误", dto.getWorkflowNo());
        }

        boolean finishCallBack;
        final WorkflowTaskVo workflowTaskVo = taskList.stream()
                .filter(task -> task.getId().equals(financeRecoOrderPo.getTaskId()))
                .findFirst().orElse(null);

        if (null != workflowTaskVo) {
            // 根据po的taskId找到的vo对象
            final int index = taskList.indexOf(workflowTaskVo);
            // 判断po之后是否还存在对象，若存在，则不是finish回调，若不存在则回调是finish
            finishCallBack = index >= taskList.size() - 1;
        } else {
            // 第一次回调时，po的taskId为空，则找不到对应的vo
            finishCallBack = false;
        }

        final LogVersionBo logVersionBo = new LogVersionBo();
        logVersionBo.setKey("操作");

        // 如果审批已经完成，当前单据应该进入下一个状态
        if (WorkflowState.FINISH.equals(workflowDetailVo.getWorkflowState()) && finishCallBack) {
            final List<WorkflowTaskStatus> taskStatusList = taskList.stream()
                    .map(WorkflowTaskVo::getStatus)
                    .collect(Collectors.toList());
            FinanceRecoOrderStatus financeRecoOrderStatus;
            // 审批拒绝,拒绝后当前操作人变更为跟单
            if (taskStatusList.contains(WorkflowTaskStatus.REJECTED)) {
                financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toWaitSubmit();
                financeRecoOrderPo.setCtrlUser(financeRecoOrderPo.getFollowUser());
                logVersionBo.setValue("审批拒绝");
            } else if (taskStatusList.contains(WorkflowTaskStatus.APPROVED)) {
                financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toComplete();
                financeRecoOrderPo.setCompleteTime(LocalDateTime.now());
                financeRecoOrderPo.setCtrlUser(workflowTaskVo.getTaskUserCode());
                logVersionBo.setValue("审批通过");
            } else {
                throw new BizException("对账审批:{}数据异常", dto.getWorkflowNo());
            }

            // 若当前单据状态已经流转，无需抛异常重试
            if (financeRecoOrderStatus == null) {
                log.warn("对账单状态为空，飞书回调重复处理，对账单据号:{}", financeRecoOrderPo.getFinanceRecoOrderNo());
                return;
            }
            financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
            // 日志
            // 设置当前操作人为飞书操作的用户
            GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
            GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                    financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                    Collections.singletonList(logVersionBo));
        } else if (WorkflowState.TERMINATE.equals(workflowDetailVo.getWorkflowState())) {
            FinanceRecoOrderStatus financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toWaitSubmit();
            if (null == financeRecoOrderStatus) {
                log.warn("飞书回调重复处理，对账单号:{}", financeRecoOrderPo.getFinanceRecoOrderNo());
                return;
            }
            logVersionBo.setValue("审批");
            financeRecoOrderPo.setCtrlUser(financeRecoOrderPo.getFollowUser());
            financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);
            // 设置当前操作人为飞书操作的用户
            logBaseService.simpleLog(LogBizModule.PREPAYMENT_LIST_SIMPLE, ScmConstant.PREPAYMENT_LOG_VERSION,
                    financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                    Collections.singletonList(logVersionBo));
        } else {
            WorkflowTaskVo nextWorkflowTask;
            if (null == workflowTaskVo) {
                nextWorkflowTask = taskList.get(0);
                final WorkflowTimelineVo workflowTimelineVo = workflowDetailVo.getTimeline().get(0);
                GlobalContext.setUserKey(workflowTimelineVo.getApproveUserCode());
                GlobalContext.setUsername(workflowTimelineVo.getApproveUsername());
            } else {
                final int index = taskList.indexOf(workflowTaskVo);
                nextWorkflowTask = taskList.get(Math.min(index + 1, taskList.size() - 1));
                GlobalContext.setUserKey(workflowTaskVo.getTaskUserCode());
                GlobalContext.setUsername(workflowTaskVo.getTaskUsername());
            }
            // 若taskId相同，则无需处理
            if (nextWorkflowTask.getId().equals(financeRecoOrderPo.getTaskId())) {
                log.info("taskId:{}相同po:{}，则无需重复处理", nextWorkflowTask.getId(), financeRecoOrderPo);
                return;
            }

            financeRecoOrderPo.setCtrlUser(nextWorkflowTask.getTaskUserCode());
            financeRecoOrderPo.setTaskId(nextWorkflowTask.getId());
            FinanceRecoOrderStatus financeRecoOrderStatus = financeRecoOrderPo.getFinanceRecoOrderStatus().toUnderReview();
            financeRecoOrderPo.setFinanceRecoOrderStatus(financeRecoOrderStatus);

            logVersionBo.setValue("审批");
            // 日志
            logBaseService.simpleLog(LogBizModule.FINANCE_RECO_ORDER_STATUS, ScmConstant.RECO_ORDER_LOG_VERSION,
                    financeRecoOrderPo.getFinanceRecoOrderNo(), financeRecoOrderStatus.getRemark(),
                    Collections.singletonList(logVersionBo));
        }

        financeRecoOrderDao.updateByIdVersion(financeRecoOrderPo);
    }

    @Override
    public FeishuWorkflowType getHandlerType() {
        return FeishuWorkflowType.IBFS_RECO_ORDER_APPROVE;
    }
}
