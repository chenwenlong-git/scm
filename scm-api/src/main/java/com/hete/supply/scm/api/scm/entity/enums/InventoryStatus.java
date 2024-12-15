package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum InventoryStatus implements IRemark {
    // 库存状态
    TO_BE_MAINTENANCE("待维护"),
    NO_MAINTENANCE("无需维护"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
