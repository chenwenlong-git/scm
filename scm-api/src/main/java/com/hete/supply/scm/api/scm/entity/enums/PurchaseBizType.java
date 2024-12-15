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
public enum PurchaseBizType implements IRemark {
    // 拆分类型
    NO_TYPE("无类型", ""),
    PROCESS("加工采购", "JG"),
    PRODUCT("大货采购", "DH"),
    ;

    private static final Map<String, PurchaseBizType> PURCHASE_BIZ_TYPE_MAP = new HashMap<>();


    private final String remark;
    private final String orderNoPrefix;

    static {
        for (PurchaseBizType value : values()) {
            PURCHASE_BIZ_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static PurchaseBizType getByRemark(String remark) {
        return PURCHASE_BIZ_TYPE_MAP.get(remark);
    }
}
