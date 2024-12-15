package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum FinanceRecoPayType implements IRemark {

    // 对账单详情的应付、应收类型
    HANDLE("应付"),
    RECEIVABLE("应收"),
    ;

    private final String remark;

}
