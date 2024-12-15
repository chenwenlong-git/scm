package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/8/7.
 */
@Getter
@AllArgsConstructor
public enum SysConfigConstant implements IRemark {
    //供应商产能筛选条件
    SUPPLIER_CAPACITY_FILTER_CONDITION("supplier_capacity_filter_condition", "供应商产能筛选条件");

    private final String configKey;
    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
