package com.hete.supply.scm.server.scm.adjust.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/6/20 11:37
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceItemStatus implements IRemark {
    // 审批状态:TO_BE_APPROVE(待审批)APPROVE_PASSED(审批通过),APPROVE_REJECT(审批拒绝),
    TO_BE_APPROVE("待审批"),
    APPROVE_PASSED("审批通过"),
    APPROVE_REJECT("审批拒绝"),
    ;

    private final String remark;
}
