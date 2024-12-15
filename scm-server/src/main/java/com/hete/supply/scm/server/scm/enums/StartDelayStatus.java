package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排产计划开始时间是否延期枚举
 *
 * @author yanjiawei
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum StartDelayStatus implements IRemark {
    // 开始时间如期
    ON_TIME("按时开始"),
    // 开始时间延误
    DELAYED("开始延误");
    private final String remark;
}
