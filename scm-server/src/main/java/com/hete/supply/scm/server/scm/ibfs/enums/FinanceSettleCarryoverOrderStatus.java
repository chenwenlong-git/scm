package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/5/25.
 */
@Getter
@AllArgsConstructor
public enum FinanceSettleCarryoverOrderStatus implements IRemark {
    // 结转单状态
    PENDING_CARRYOVER("待结转"),
    CARRIED_OVER("已结转"),
    ;

    private final String remark;
}

