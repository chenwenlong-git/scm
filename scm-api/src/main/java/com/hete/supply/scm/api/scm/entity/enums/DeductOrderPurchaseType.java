package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum DeductOrderPurchaseType implements IRemark {
    //类型：
    PRODUCT_PURCHASE("大货采购单"),
    PROCESS_PURCHASE("加工采购单"),
    PURCHASE_RETURN("退货单"),
    SAMPLE_RETURN("样品退货单"),
    SAMPLE("样品单"),
    DELIVER("发货单");

    private final String name;

    private static final Map<String, DeductOrderPurchaseType> DEDUCT_ORDER_PURCHASE_TYPE_MAP = new HashMap<>();


    static {
        for (DeductOrderPurchaseType value : values()) {
            DEDUCT_ORDER_PURCHASE_TYPE_MAP.put(value.getRemark(), value);
        }
    }


    public static DeductOrderPurchaseType getByDesc(String desc) {
        return DEDUCT_ORDER_PURCHASE_TYPE_MAP.get(desc);
    }

    public static DeductOrderPurchaseType convertDeductType(SupplementOrderPurchaseType supplementOrderPurchaseType) {
        if (SupplementOrderPurchaseType.PRODUCT_PURCHASE.equals(supplementOrderPurchaseType)) {
            return DeductOrderPurchaseType.PRODUCT_PURCHASE;
        }

        if (SupplementOrderPurchaseType.PROCESS_PURCHASE.equals(supplementOrderPurchaseType)) {
            return DeductOrderPurchaseType.PROCESS_PURCHASE;
        }

        if (SupplementOrderPurchaseType.SAMPLE.equals(supplementOrderPurchaseType)) {
            return DeductOrderPurchaseType.SAMPLE;
        }

        if (SupplementOrderPurchaseType.DELIVER.equals(supplementOrderPurchaseType)) {
            return DeductOrderPurchaseType.DELIVER;
        }

        if (SupplementOrderPurchaseType.PURCHASE_RETURN.equals(supplementOrderPurchaseType)) {
            return DeductOrderPurchaseType.PURCHASE_RETURN;
        }

        throw new BizException("错误的状态转换，请联系系统管理员");
    }

    @Override
    public String getRemark() {
        return this.name;
    }
}
