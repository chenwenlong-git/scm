package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2023/12/15.
 */
@Getter
@AllArgsConstructor
public enum CommissionAttribute implements IRemark {
    // 提成属性
    FIRST_LEVEL(1, "一阶提成"),
    SECOND_LEVEL(2, "二阶提成"),
    // 其他提成属性...
    ;

    private final int commissionLevel;
    private final String remark;

    public static CommissionAttribute findByCommissionLevel(Integer commissionLevel) {
        for (CommissionAttribute attribute : values()) {
            if (Objects.equals(attribute.getCommissionLevel(), commissionLevel)) {
                return attribute;
            }
        }
        // 如果找不到对应的枚举值，可以选择抛出异常或返回一个默认值，具体取决于你的需求。
        throw new IllegalArgumentException("Invalid CommissionAttribute value: " + commissionLevel);
    }
}
