package com.hete.supply.scm.server.scm.purchase.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/3/20 10:50
 */
@Getter
@AllArgsConstructor
public enum PurchaseSplitRaw implements IRemark {
    // 采购拆单补交原料处理策略
    BEFORE_DELIVER_SUPPLIER("待排产/待投产且原料来源=供应商"),
    BEFORE_DELIVER_COMPANY_OR_OTHER("待排产/待投产且原料来源=其他供应商/我司"),
    AFTER_DELIVER("原料出库以后"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
