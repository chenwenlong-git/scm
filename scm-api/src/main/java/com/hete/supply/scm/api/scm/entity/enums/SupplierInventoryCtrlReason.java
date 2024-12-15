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
public enum SupplierInventoryCtrlReason implements IRemark {
    // 供应商库存操作原因
    PROCESS("加工采购"),
    PRODUCT_COMMISSIONING("大货采购确认投产"),
    DELIVER_COMMISSIONING("大货采购生成发货单"),
    STOCK_UP("半成品备货"),
    SUPPLIER_CTRL("供应商操作"),
    PURCHASE_RAW_RETURN("采购原料归还"),
    REFUSE_RECEIVE("供应商拒绝接单"),
    FOLLOW_CONFIRM("采购跟单确认"),
    PURCHASE_CANCEL("采购单作废"),
    PURCHASE_BACK("采购单回退"),
    SPLIT_ORDER("拆单补交"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
