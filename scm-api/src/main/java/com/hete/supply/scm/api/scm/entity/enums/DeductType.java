package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum DeductType implements IRemark {
    //扣款单的类型
    PRICE("价差扣款"),
    PROCESS("加工扣款"),
    QUALITY("品质扣款"),
    OTHER("其他"),
    PAY("预付款"),
    DEFECTIVE_RETURN("次品退供");

    private final String name;

    private static final Map<String, DeductType> DEDUCT_TYPE_MAP = new HashMap<>();


    static {
        for (DeductType value : values()) {
            DEDUCT_TYPE_MAP.put(value.getRemark(), value);
        }
    }


    public static DeductType getByDesc(String desc) {
        return DEDUCT_TYPE_MAP.get(desc);
    }

    @Override
    public String getRemark() {
        return this.name;
    }
}
