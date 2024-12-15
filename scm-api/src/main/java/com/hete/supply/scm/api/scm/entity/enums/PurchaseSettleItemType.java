package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum PurchaseSettleItemType implements IRemark {
    //采购结算单明细类型:单据类型
    PRODUCT_PURCHASE("大货采购单"),
    PROCESS_PURCHASE("加工采购单"),
    SAMPLE("样品单"),
    REPLENISH("补款单"),
    DEDUCT("扣款单"),
    DELIVER("发货单");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public static PurchaseSettleItemType getProductPurchaseByProduct(PurchaseBizType purchaseBizType) {
        if (purchaseBizType.equals(PurchaseBizType.PRODUCT)) {
            return PurchaseSettleItemType.PRODUCT_PURCHASE;
        }
        if (purchaseBizType.equals(PurchaseBizType.PROCESS)) {
            return PurchaseSettleItemType.PROCESS_PURCHASE;
        }
        throw new BizException("类型错误，采购子单存在存在非法类型");
    }

}
