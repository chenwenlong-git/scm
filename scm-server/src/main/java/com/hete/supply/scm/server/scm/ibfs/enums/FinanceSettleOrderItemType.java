package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/5/23.
 */
@Getter
@AllArgsConstructor
public enum FinanceSettleOrderItemType implements IRemark {
    // 结算单明细类型
    RECO_ORDER("对账单"),
    CARRYOVER_ORDER("结转单"),
    ;

    private final String remark;
}
