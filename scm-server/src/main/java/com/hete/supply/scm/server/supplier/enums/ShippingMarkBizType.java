package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/2/14 01:09
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum ShippingMarkBizType implements IRemark {
    // 箱唛单据类型:PURCHASE_CHILD(采购子单),SAMPLE_CHILD(样品子单),
    PURCHASE_CHILD("采购子单"),
    SAMPLE_CHILD("样品子单"),
    ;

    private String remark;
}
