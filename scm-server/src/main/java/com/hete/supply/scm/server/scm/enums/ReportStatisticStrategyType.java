package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@Getter
@AllArgsConstructor
public enum ReportStatisticStrategyType implements IRemark {
    // 报表策略枚举
    PROC_LABOR_EFFICIENCY_DAILY("加工部日维度人效报表"),
    PROC_LABOR_EFFICIENCY_MONTHLY("加工部月维度人效报表"),
    PROC_PROCEDURE_EFFICIENCY_DAILY("加工工序日维度人效报表"),
    PROC_PROCEDURE_EFFICIENCY_MONTHLY("加工工序月维度人效报表"),

    PROC_PLAT_PD_DAILY("各平台加工需求（当月）"),
    PROC_PLAT_PS_DAILY("各平台出库数（当月）"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return desc;
    }
}
