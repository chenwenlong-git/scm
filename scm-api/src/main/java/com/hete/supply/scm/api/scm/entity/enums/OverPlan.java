package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @date 2023/07/23 12:45
 */
@Getter
@AllArgsConstructor
public enum OverPlan implements IRemark {
    // 是否超额
    TRUE("已超额"),
    FALSE("未超额"),
    ;
    private final String remark;
}