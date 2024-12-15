package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2024/6/13.
 */
@Getter
@RequiredArgsConstructor
public enum QcSourceOrderType implements IRemark {
    // 来源单号类型
    REPAIR_ORDER_NO("返修单"),
    PROCESS_ORDER_NO("加工单"),
    PURCHASE_CHILD_ORDER_NO("采购订单"),
    OUTBOUND_ORDER_NO("出库单"),
    ALLOCATION_ORDER_NO("调拨单"),
    RECEIVE_ORDER_NO("收货单"),

    ;
    private final String remark;
}
