package com.hete.supply.scm.server.scm.purchase.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2023/3/28 19:49
 */
@AllArgsConstructor
@Getter
public enum OrderType implements IRemark {

    // 单据类型
    PURCHASE("采购"),
    PROCESS("加工");

    private static final Map<String, OrderType> ORDER_TYPE_MAP = new HashMap<>();


    private final String remark;

    static {
        for (OrderType value : values()) {
            ORDER_TYPE_MAP.put(value.getRemark(), value);
        }
    }

    public static OrderType getByRemark(String remark) {
        return ORDER_TYPE_MAP.get(remark);
    }
}
