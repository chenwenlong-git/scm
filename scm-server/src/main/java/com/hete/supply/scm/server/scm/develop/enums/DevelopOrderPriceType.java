package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/3/25 14:56
 */
@AllArgsConstructor
@Getter
public enum DevelopOrderPriceType implements IRemark {

    // 开发单相关单据价格的类型
    SUPPLIER_SAMPLE_PURCHASE_PRICE("样品单供应商报价大货价格"),
    DEVELOP_PURCHASE_PRICE("开发子单大货价格(一个开发子单只有一条渠道价格)"),
    PRICING_PURCHASE_PRICE("核价单需要打样大货价格"),
    PRICING_NOT_PURCHASE_PRICE("核价单无需打样大货价格"),
    SAMPLE_PURCHASE_PRICE("样品单大货价格、产前样审版单大货价格"),
    ;


    private final String remark;

}
