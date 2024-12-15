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
public enum PurchaseDemandType implements IRemark {
    // 采购需求类型
    NORMAL("常规"),
    WH("网红"),
    REPAIR("返修"),
    SPECIAL("特殊"),
    ;


    private static final Map<String, PurchaseDemandType> PURCHASE_DEMAND_TYPE_MAP = new HashMap<>();


    private final String remark;

    static {
        for (PurchaseDemandType value : values()) {
            PURCHASE_DEMAND_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static PurchaseDemandType getByRemark(String remark) {
        return PURCHASE_DEMAND_TYPE_MAP.get(remark);
    }
}
