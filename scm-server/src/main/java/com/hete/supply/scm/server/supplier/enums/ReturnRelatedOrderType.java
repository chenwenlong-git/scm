package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author rockyHuas
 * @date 2023/06/27 16:55
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum ReturnRelatedOrderType implements IRemark {
    // 关联单据类型:DEDUCT_ORDER(扣款单),DELIVER_ORDER(发货单),
    DEDUCT_ORDER("扣款单"),
    DELIVER_ORDER("发货单"),
    ;

    private String remark;

}
