package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/12/2 20:24
 */
@AllArgsConstructor
@Getter
public enum UseType implements IRemark {
    // 使用类型:COMMONLY_USED(常用),NOT_COMMONLY_USED(不常用),
    COMMONLY_USED("常用"),
    NOT_COMMONLY_USED("不常用"),
    ;

    private final String remark;

    private static final Map<String, UseType> USE_TYPE_MAP = new HashMap<>();

    static {
        for (UseType value : values()) {
            USE_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static UseType getByRemark(String remark) {
        return USE_TYPE_MAP.get(remark);
    }
}
