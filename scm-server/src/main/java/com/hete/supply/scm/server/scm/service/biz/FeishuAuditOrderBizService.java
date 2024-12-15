package com.hete.supply.scm.server.scm.service.biz;

import com.hete.supply.mc.api.workflow.entity.dto.WorkflowMqCallbackDto;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.server.scm.enums.FeishuWorkflowType;
import com.hete.supply.scm.server.scm.feishu.dao.FeishuAuditOrderDao;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.supply.scm.server.scm.handler.WorkflowMqBackStrategy;
import com.hete.support.api.exception.BizException;
import com.hete.support.core.handler.HandlerContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ChenWenLong
 * @date 2023/12/8 15:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeishuAuditOrderBizService {

    private final FeishuAuditOrderDao feishuAuditOrderDao;

    /**
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/12/8 15:10
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#dto.getWorkflowReqNo()", prefix = ScmRedisConstant.FEISHU_AUDIT_ORDER_PREFIX, waitTime = 4)
    public void handleMessage(WorkflowMqCallbackDto dto) {
        log.info("飞书审批单审核结果: 单号{}, dto={}", dto.getWorkflowReqNo(), JacksonUtil.parse2Str(dto));
        FeishuWorkflowType feishuWorkflowType = FeishuWorkflowType.contains(dto.getWorkflowType());
        if (feishuWorkflowType == null) {
            log.info("飞书审批单审核结果找不到对应审批类型：{}", dto.getWorkflowType());
            return;
        }
        FeishuAuditOrderPo feishuAuditOrderPo = feishuAuditOrderDao.getOneByFeishuAuditOrderNo(dto.getWorkflowReqNo());
        if (feishuAuditOrderPo == null) {
            throw new BizException("飞书审批单{}进行回调MQ查询不到对应信息！", dto.getWorkflowReqNo());
        }
        String latestRemark = dto.getLatestRemark();
        if (StringUtils.isNotBlank(dto.getLatestRemark()) && dto.getLatestRemark().length() > 500) {
            log.error("书审批单{}审批意见文本长度超过500，自动截取前500个字符。审批原文={}", dto.getWorkflowReqNo(), dto.getLatestRemark());
            latestRemark = dto.getLatestRemark().substring(0, 500);
        }

        feishuAuditOrderPo.setWorkflowNo(dto.getWorkflowNo());
        feishuAuditOrderPo.setWorkflowTitle(dto.getWorkflowTitle());
        feishuAuditOrderPo.setWorkflowState(dto.getWorkflowState());
        feishuAuditOrderPo.setWorkflowResult(dto.getWorkflowResult());
        feishuAuditOrderPo.setWorkflowType(dto.getWorkflowType());
        feishuAuditOrderPo.setProcessBusinessId(dto.getProcessBusinessId());
        feishuAuditOrderPo.setOriginatorUser(dto.getOriginatorUser());
        feishuAuditOrderPo.setFinishTime(dto.getFinishTime());
        feishuAuditOrderPo.setFailMsg(dto.getFailMsg());
        feishuAuditOrderPo.setLatestRemark(latestRemark);
        feishuAuditOrderPo.setLatestOperateUser(dto.getLatestOperateUser());
        feishuAuditOrderPo.setLatestOperateUsername(dto.getLatestOperateUsername());
        feishuAuditOrderDao.updateByIdVersion(feishuAuditOrderPo);

        // 审批单回调之后相关业务单据逻辑处理
        WorkflowMqBackStrategy handlerBean = HandlerContext.getHandlerBean(WorkflowMqBackStrategy.class, feishuWorkflowType);
        handlerBean.workflowMqBackHandleBusiness(dto, feishuAuditOrderPo);

    }

}
