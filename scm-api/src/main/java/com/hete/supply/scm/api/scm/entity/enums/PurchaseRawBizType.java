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
public enum PurchaseRawBizType implements IRemark {
    // 采购原料类型:DEMAND(需求原料),ACTUAL_DELIVER(实发原料),
    // 记录原料主要用于供应商或其他供应商提供原料时，记录到对应的采购原料数据表中
    FORMULA("原料配方"),
    DEMAND("需求原料"),
    ACTUAL_DELIVER("实发原料"),
    RECORD("记录原料"),
    RECORD_BOM("记录原料bom"),

    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    private static final Map<String, PurchaseRawBizType> SAMPLE_DEV_TYPE_MAP = new HashMap<>();

    static {
        for (PurchaseRawBizType value : values()) {
            SAMPLE_DEV_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static PurchaseRawBizType getByRemark(String remark) {
        return SAMPLE_DEV_TYPE_MAP.get(remark);
    }
}
