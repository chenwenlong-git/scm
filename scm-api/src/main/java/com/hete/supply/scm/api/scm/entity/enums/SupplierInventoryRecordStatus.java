package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/4/8.
 */
@Getter
@AllArgsConstructor
public enum SupplierInventoryRecordStatus implements IRemark {
    // 供应商库存变更记录状态
    PENDING_APPROVAL("待审核"),
    EFFECTIVE("已生效"),
    REJECTED("已拒绝"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
