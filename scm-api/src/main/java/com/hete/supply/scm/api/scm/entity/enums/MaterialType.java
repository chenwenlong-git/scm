package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/12/2 20:16
 */
@AllArgsConstructor
@Getter
public enum MaterialType implements IRemark {
    // 辅料类型:BASE(基础类型),COMBINE(组合产品),
    BASE("基础类型"),
    COMBINE("组合产品"),
    ;

    private final String remark;

    private static final Map<String, MaterialType> MATERIAL_TYPE_MAP = new HashMap<>();

    static {
        for (MaterialType value : values()) {
            MATERIAL_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static MaterialType getByRemark(String remark) {
        return MATERIAL_TYPE_MAP.get(remark);
    }
}
