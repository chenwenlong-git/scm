package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplierStatus implements IRemark {
    //供应商状态
    ENABLED("启用"),
    DISABLED("禁用");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public SupplierStatus toOpen() {
        if (ENABLED == this) {
            throw new BizException("状态处于{}时才进行操作", ENABLED.getRemark());
        }
        return ENABLED;
    }

    public SupplierStatus toClose() {
        if (DISABLED == this) {
            throw new BizException("状态处于{}时才进行操作", DISABLED.getRemark());
        }
        return DISABLED;
    }
}
