package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 原料是否满足枚举
 *
 * @author yanjiawei
 * Created on 2024/8/8.
 */
@Getter
@AllArgsConstructor
public enum IsMaterialStockSatisfy implements IRemark {
    SATISFIED("满足"),
    UNSATISFIED("不满足");

    private final String remark;

    public String getRemark() {
        return remark;
    }
}
