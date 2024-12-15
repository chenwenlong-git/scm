package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplierWarehouse implements IRemark {
    // 供应商仓库
    STOCK_UP("备货仓"),
    SELF_PROVIDE("自备仓"),
    VIRTUAL_WAREHOUSE("虚拟仓"),
    DEFECTIVE_WAREHOUSE("不良仓"),
    ;

    private final String name;

    private static final Map<String, SupplierWarehouse> SUPPLIER_WAREHOUSE_MAP = new HashMap<>();


    static {
        for (SupplierWarehouse value : values()) {
            SUPPLIER_WAREHOUSE_MAP.put(value.getName(), value);
        }
    }


    public static SupplierWarehouse getByName(String name) {
        return SUPPLIER_WAREHOUSE_MAP.get(name);
    }

    @Override
    public String getRemark() {
        return this.name;
    }
}
