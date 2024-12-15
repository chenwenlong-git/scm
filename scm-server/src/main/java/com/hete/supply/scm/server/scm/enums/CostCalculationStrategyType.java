package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/1/31.
 */
@Getter
@AllArgsConstructor
public enum CostCalculationStrategyType implements IRemark {
    // 计算成本策略类型
    NON_REWORK_PROCESS_ORDER("非返工加工单"),
    REWORK_PROCESS_ORDER("返工加工单"),
    REPAIR_ORDER("返修单");
    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
