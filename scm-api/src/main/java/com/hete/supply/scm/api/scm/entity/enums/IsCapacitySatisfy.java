package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 产能是否满足枚举
 *
 * @author yanjiawei
 * Created on 2024/8/8.
 */
@Getter
@AllArgsConstructor
public enum IsCapacitySatisfy implements IRemark {
    //满足
    SATISFIED("满足"),
    //不满足
    UNSATISFIED("不满足"),
    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
