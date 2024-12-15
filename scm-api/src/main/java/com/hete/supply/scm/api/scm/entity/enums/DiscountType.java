package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@AllArgsConstructor
@Getter
public enum DiscountType implements IRemark {
    // 优惠类型:NO_DISCOUNT(无折扣),PROVIDE_RAW(我方提供原料),
    NO_DISCOUNT("无折扣"),
    PROVIDE_RAW("我方提供原料"),
    ;

    private final String name;

    private static final Map<String, DiscountType> DISCOUNT_TYPE_MAP = new HashMap<>();

    static {
        for (DiscountType value : values()) {
            DISCOUNT_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    @Override
    public String getRemark() {
        return this.name;
    }

    public static DiscountType getByRemark(String remark) {
        return DISCOUNT_TYPE_MAP.get(remark);
    }
}
