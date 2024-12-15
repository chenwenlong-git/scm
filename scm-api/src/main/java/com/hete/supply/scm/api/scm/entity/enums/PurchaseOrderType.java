package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2023/8/24 10:17
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderType implements IRemark {
    // 采购单类型:FIRST_ORDER(首单),PRENATAL(产前样),NORMAL(常规),REPAIR(返修)
    FIRST_ORDER("首单"),
    PRENATAL("产前样"),
    NORMAL("常规"),
    WH("网红"),
    REPAIR("返修"),
    SPECIAL("特殊"),
    ;
    private final String remark;
}
