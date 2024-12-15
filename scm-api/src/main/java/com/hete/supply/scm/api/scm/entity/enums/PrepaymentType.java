package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:37
 */
@Getter
@AllArgsConstructor
public enum PrepaymentType implements IRemark {
    // 预付类型:PREPAYMENT_OF_GOODS(预付货款),
    PREPAYMENT_OF_GOODS("预付货款"),
    ;

    private final String remark;

}
