package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/11/2 15:23
 */
@Getter
@AllArgsConstructor
public enum SampleRawBizType implements IRemark {
    // 原料类型
    FORMULA("原料配方"),
    ACTUAL_DELIVER("实发原料"),
    DEMAND("需求原料"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    private static final Map<String, SampleRawBizType> SAMPLE_DEV_TYPE_MAP = new HashMap<>();

    static {
        for (SampleRawBizType value : values()) {
            SAMPLE_DEV_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static SampleRawBizType getByRemark(String remark) {
        return SAMPLE_DEV_TYPE_MAP.get(remark);
    }
}
