package com.hete.supply.scm.server.scm.purchase.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/3/28 19:49
 */
@AllArgsConstructor
@Getter
public enum PurchaseReturnPlacedCnt implements IRemark {

    // 采购是否返还sku可拆单数
    TRUE("返还"),
    FALSE("不返还");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
