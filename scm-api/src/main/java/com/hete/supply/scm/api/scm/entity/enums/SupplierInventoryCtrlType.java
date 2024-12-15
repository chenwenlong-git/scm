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
public enum SupplierInventoryCtrlType implements IRemark {
    // 供应商库存操作类型
    CHECK("盘点"),
    OUTBOUND("出库"),
    WAREHOUSING("入库"),
    CAMP_ON("预占"),
    RELEASE("释放"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
