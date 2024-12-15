package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplementOrderPurchaseType implements IRemark {
    //补款单采购明细关联单据类型：
    PRODUCT_PURCHASE("大货采购单"),
    PROCESS_PURCHASE("加工采购单"),
    SAMPLE("样品单"),
    DELIVER("发货单"),
    PURCHASE_RETURN("退货单");

    private final String name;


    @Override
    public String getRemark() {
        return this.name;
    }
}
