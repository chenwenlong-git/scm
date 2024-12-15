package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排产策略枚举
 *
 * @author yanjiawei
 * Created on 2023/8/31.
 */
@AllArgsConstructor
@Getter
public enum ProcessPlanStrategy implements IRemark {
    // 最早开始时间和结束时间排产
    EARLIEST("最早时间策略"),
    // 非最早开始时间和结束时间排产
    NON_EARLIEST("非最早时间策略");
    private final String remark;
}
