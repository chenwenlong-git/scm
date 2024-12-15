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
public enum InventoryApproveResult implements IRemark {
    // 库存变更审核结果
    APPROVED("通过"),
    REJECTED("拒绝");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
