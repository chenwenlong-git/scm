package com.hete.supply.scm.server.scm.feishu.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.mc.api.workflow.enums.WorkflowResult;
import com.hete.supply.mc.api.workflow.enums.WorkflowState;
import com.hete.supply.scm.server.scm.enums.FeishuAuditOrderType;
import com.hete.supply.scm.server.scm.feishu.entity.po.FeishuAuditOrderPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 飞书审批单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-05
 */
@Component
@Validated
public class FeishuAuditOrderDao extends BaseDao<FeishuAuditOrderMapper, FeishuAuditOrderPo> {

    public Map<Long, List<FeishuAuditOrderPo>> getMapByBusinessIdList(List<Long> businessIdList) {
        if (CollectionUtils.isEmpty(businessIdList)) {
            return Collections.emptyMap();
        }
        return list(Wrappers.<FeishuAuditOrderPo>lambdaQuery()
                .in(FeishuAuditOrderPo::getBusinessId, businessIdList)
                .orderByDesc(FeishuAuditOrderPo::getCreateTime))
                .stream()
                .collect(Collectors.groupingBy(FeishuAuditOrderPo::getBusinessId));
    }

    public List<FeishuAuditOrderPo> getByBusinessIdAndStatusList(Long businessId, List<WorkflowState> statusList) {
        if (businessId == null) {
            return Collections.emptyList();
        }
        return list(Wrappers.<FeishuAuditOrderPo>lambdaQuery()
                .eq(FeishuAuditOrderPo::getBusinessId, businessId)
                .in(FeishuAuditOrderPo::getWorkflowState, statusList));
    }

    public FeishuAuditOrderPo getOneByFeishuAuditOrderNo(String feishuAuditOrderNo) {
        if (StringUtils.isBlank(feishuAuditOrderNo)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.<FeishuAuditOrderPo>lambdaQuery()
                .eq(FeishuAuditOrderPo::getFeishuAuditOrderNo, feishuAuditOrderNo));
    }

    public Long getApproveFailTimes(LocalDateTime startTime, LocalDateTime endTime,
                                    FeishuAuditOrderType feishuAuditOrderType, WorkflowResult workflowResult) {

        return baseMapper.selectCount(Wrappers.<FeishuAuditOrderPo>lambdaQuery()
                .eq(FeishuAuditOrderPo::getFeishuAuditOrderType, feishuAuditOrderType)
                .eq(FeishuAuditOrderPo::getWorkflowResult, workflowResult)
                .ge(FeishuAuditOrderPo::getCreateTime, startTime)
                .le(FeishuAuditOrderPo::getCreateTime, endTime));
    }
}
