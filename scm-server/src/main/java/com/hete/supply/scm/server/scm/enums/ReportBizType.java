package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/3/15.
 */
@AllArgsConstructor
@Getter
public enum ReportBizType implements IRemark {

    // 加工报表业务类型
    PROC_LABOR_EFFICIENCY_REPORT("加工部人效报表"),
    PROC_PROCEDURE_EFFICIENCY_REPORT("加工工序人效报表"),

    PROC_DAILY_PlAT_PD_REPORT("加工部每日需求报表"),
    PROC_DAILY_PlAT_PS_REPORT("加工部每日出库数报表"),
    ;

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
