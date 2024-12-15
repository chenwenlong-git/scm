package com.hete.supply.scm.server.scm.adjust.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowDetailVo;
import com.hete.supply.mc.api.workflow.entity.vo.WorkflowTaskVo;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveDao;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveStatus;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.WorkflowMqBackStrategy;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.handler.HandlerContext;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hete.supply.mc.api.workflow.enums.WorkflowResult.AGREE;
import static com.hete.supply.mc.api.workflow.enums.WorkflowResult.REFUSE;

/**
 * @author weiwenxin
 * @date 2024/6/20 17:02
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustWorkflowStrategy implements WorkflowMqBackStrategy {
    private final AdjustPriceApproveDao adjustPriceApproveDao;
    private final McRemoteService mcRemoteService;

    @RedisLock(prefix = ScmRedisConstant.ADJUST_PRICE_CALLBACK, key = "#dto.workflowNo", waitTime = 1, leaseTime = -1)
    @Override
    public void workflowMqBackHandleBusiness(WorkflowMqCallbackDto dto, FeishuAuditOrderPo feishuAuditOrderPo) {
        log.info("调价审批飞书回调:{}", dto);
        final AdjustPriceApprovePo adjustPriceApprovePo = adjustPriceApproveDao.getById(feishuAuditOrderPo.getBusinessId());
        if (null == adjustPriceApprovePo) {
            throw new BizException("调价审批单:{}不存在，飞书回调数据异常", feishuAuditOrderPo.getBusinessId());
        }
        final WorkflowDetailVo workflowDetailVo = mcRemoteService.workFlowDetail(dto.getWorkflowNo());
        if (null == workflowDetailVo) {
            throw new BizException("飞书审批:{}不存在，数据错误", dto.getWorkflowNo());
        }
        final List<WorkflowTaskVo> taskList = workflowDetailVo.getTaskList();
        if (CollectionUtils.isEmpty(taskList)) {
            throw new BizException("飞书审批:{}不存在审批节点，数据错误", dto.getWorkflowNo());
        }
        final WorkflowTaskVo workflowTaskVo = taskList.get(taskList.size() - 1);
        AdjustPriceStrategy handlerBean = HandlerContext.getHandlerBean(AdjustPriceStrategy.class, adjustPriceApprovePo.getApproveType());
        if (WorkflowState.START.equals(dto.getWorkflowState())) {
            adjustPriceApprovePo.setWorkflowNo(dto.getWorkflowNo());
            final ApproveStatus approveStatus = adjustPriceApprovePo.getApproveStatus().toApproving();
            if (workflowTaskVo.getId().equals(adjustPriceApprovePo.getTaskId())) {
                log.info("调价审批飞书回调已处理，重复回调！");
                return;
            }
            adjustPriceApprovePo.setApproveStatus(approveStatus);
            adjustPriceApprovePo.setTaskId(workflowTaskVo.getId());
            adjustPriceApprovePo.setApproveUser(workflowTaskVo.getTaskUserCode());
            adjustPriceApprovePo.setApproveUsername(workflowTaskVo.getTaskUsername());
        } else if (WorkflowState.FINISH.equals(workflowDetailVo.getWorkflowState())) {
            if (AGREE.equals(dto.getWorkflowResult())) {
                final ApproveStatus approveStatus = adjustPriceApprovePo.getApproveStatus().toPassed();
                if (null == approveStatus) {
                    log.info("调价审批飞书回调已处理，重复回调！");
                    return;
                }
                adjustPriceApprovePo.setApproveStatus(approveStatus);
                adjustPriceApprovePo.setApproveUser("");
                adjustPriceApprovePo.setApproveUsername("");
                handlerBean.agreeHandle(adjustPriceApprovePo);
            }
            if (REFUSE.equals(dto.getWorkflowResult())) {
                final ApproveStatus approveStatus = adjustPriceApprovePo.getApproveStatus().toReject();
                if (null == approveStatus) {
                    log.info("调价审批飞书回调已处理，重复回调！");
                    return;
                }
                adjustPriceApprovePo.setApproveStatus(approveStatus);
                adjustPriceApprovePo.setApproveUser("");
                adjustPriceApprovePo.setApproveUsername("");
                handlerBean.refuseHandle(adjustPriceApprovePo);
            }
        } else if (WorkflowState.FAIL.equals(dto.getWorkflowState())
                || WorkflowState.TERMINATE.equals(dto.getWorkflowState())) {
            // 撤销、失败状态处理
            final ApproveStatus approveStatus = adjustPriceApprovePo.getApproveStatus().toFail();
            if (null == approveStatus) {
                log.info("调价审批飞书回调已处理，重复回调！");
                return;
            }
            adjustPriceApprovePo.setApproveStatus(approveStatus);
            handlerBean.failHandle(adjustPriceApprovePo);
        }

        adjustPriceApprovePo.setTaskId(workflowTaskVo.getId());
        adjustPriceApproveDao.updateByIdVersion(adjustPriceApprovePo);
    }

    @Override
    public FeishuWorkflowType getHandlerType() {
        return FeishuWorkflowType.ADJUST_PRICE_APPROVE;
    }
}
