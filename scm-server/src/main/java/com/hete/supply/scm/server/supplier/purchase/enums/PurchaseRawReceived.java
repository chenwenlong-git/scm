package com.hete.supply.scm.server.supplier.purchase.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/19 14:24
 */
@Getter
@AllArgsConstructor
public enum PurchaseRawReceived implements IRemark {
    // 原料收货类型：
    NO_NEED_TO_RECEIVE("无需收货"),
    ABSENT_ORDER("不存在原料收货单"),
    UNRECEIVED("未收货"),
    RECEIVED("已收货"),
    ;

    private final String remark;
}
