package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @date 2023/07/23 23:49
 */
@Getter
@AllArgsConstructor
public enum NeedProcessPlan implements IRemark {
    // 是否需要排产
    TRUE("需要排产"),
    FALSE("无需排产"),
    ;
    private final String remark;
}