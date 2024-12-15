package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排产计划结束时间是否延期枚举
 *
 * @author yanjiawei
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum EndDelayStatus implements IRemark {
    // 结束时间如期
    ON_TIME("按时结束"),
    // 结束时间延期
    DELAYED("结束延误");
    private final String remark;
}
