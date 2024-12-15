package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/5/17 11:00
 */
@Getter
@AllArgsConstructor
public enum PaymentBizType implements IRemark {
    // 付款业务类型:PREPAYMENT(预付款),
    PREPAYMENT("预付款"),
    SETTLEMENT("结算");
    private final String remark;

}
