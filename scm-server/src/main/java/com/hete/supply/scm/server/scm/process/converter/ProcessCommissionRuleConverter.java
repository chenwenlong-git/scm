package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.server.scm.process.entity.bo.CommissionDetailBo;
import com.hete.supply.scm.server.scm.process.entity.bo.ProcessCommissionRuleBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessCommissionRulePo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessCommissionRuleVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessOrderScanStatListVo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2023/12/15.
 */
public class ProcessCommissionRuleConverter {
    public static List<ProcessCommissionRuleVo> toProcessCommissionRuleVos(TreeSet<ProcessCommissionRuleBo> commissionRules,
                                                                           BigDecimal extraCommission) {
        if (Objects.isNull(commissionRules)) {
            return Collections.emptyList();
        }

        return commissionRules.stream()
                .map(commissionRule -> {
                    ProcessCommissionRuleVo vo = new ProcessCommissionRuleVo();
                    vo.setProcessCommissionRuleId(commissionRule.getProcessCommissionRuleId());
                    vo.setCommissionLevel(commissionRule.getCommissionLevel());
                    vo.setStartQuantity(commissionRule.getStartQuantity());
                    vo.setEndQuantity(commissionRule.getEndQuantity());
                    vo.setCommissionCoefficient(commissionRule.getCommissionCoefficient());
                    vo.setCommissionPrice(commissionRule.getCommissionPrice());
                    vo.setUnitStaircaseTotalCommissionPrice(commissionRule.getCommissionPrice()
                            .add(extraCommission));
                    vo.setVersion(commissionRule.getVersion());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public static List<ProcessCommissionRulePo> toPos(TreeSet<ProcessCommissionRuleBo> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }

        return rules.stream()
                .map(rule -> {
                    ProcessCommissionRulePo po = new ProcessCommissionRulePo();
                    po.setCommissionLevel(rule.getCommissionLevel());
                    po.setStartQuantity(rule.getStartQuantity());
                    po.setEndQuantity(rule.getEndQuantity());
                    po.setCommissionCoefficient(rule.getCommissionCoefficient());
                    return po;
                })
                .collect(Collectors.toList());
    }

    public static List<ProcessOrderScanStatListVo.ScanCommissionDetailVo> toScanCommissionDetailVos(List<CommissionDetailBo> commissionDetails) {
        if (CollectionUtils.isEmpty(commissionDetails)) {
            return Collections.emptyList();
        }

        return commissionDetails.stream()
                .map(commissionDetail -> {
                            ProcessOrderScanStatListVo.ScanCommissionDetailVo vo
                                    = new ProcessOrderScanStatListVo.ScanCommissionDetailVo();
                            vo.setScanCommissionDetailId(commissionDetail.getScanCommissionDetailId());
                            vo.setCommissionAttribute(commissionDetail.getCommissionAttribute());
                            vo.setUnitCommission(commissionDetail.getUnitCommission());
                            vo.setTotalAmount(commissionDetail.getTotalAmount());
                            return vo;
                        }

                )
                .collect(Collectors.toList());
    }
}
