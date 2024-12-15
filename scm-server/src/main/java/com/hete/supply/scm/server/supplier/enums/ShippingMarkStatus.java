package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/14 01:19
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum ShippingMarkStatus implements IRemark {
    // 箱唛状态:WAIT_DELIVER(待发货),DELIVERED(已发货),DELETED(作废),
    WAIT_DELIVER("待发货"),
    DELIVERED("已发货"),
    DELETED("作废"),
    ;
    private String remark;
}
