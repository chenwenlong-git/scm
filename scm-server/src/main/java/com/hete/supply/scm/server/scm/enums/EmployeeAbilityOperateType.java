package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @date 2023/07/29 17:45
 */
@Getter
@AllArgsConstructor
public enum EmployeeAbilityOperateType implements IRemark {
    // 产能池操作类型
    ADD("增加"),
    SUB("扣减"),
    ;
    private final String remark;
}