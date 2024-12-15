package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum SkuType implements IRemark {
    // sku类型:SKU(商品sku),SM_SKU(辅料sku),
    SKU("商品"),
    SM_SKU("辅料"),
    ;

    private final String remark;

    private static final Map<String, SkuType> SKU_TYPE_MAP = new HashMap<>();

    static {
        for (SkuType value : values()) {
            SKU_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static SkuType getByRemark(String remark) {
        return SKU_TYPE_MAP.get(remark);
    }
}
