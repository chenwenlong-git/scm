package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @date 2023/07/31 17:48
 */
@Getter
@AllArgsConstructor
public enum ProcessPlanDelay implements IRemark {
    // 排产工序延误
    TRUE("已延误"),
    FALSE("无延误"),
    ;
    private final String remark;
}