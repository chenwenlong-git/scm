package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2023/8/4 17:34
 */
@Getter
@AllArgsConstructor
public enum ReviewResult implements IRemark {
    // 审版结果
    REVIEW_PASS("审版通过"),
    REVIEW_NO_PASS("审版不通过"),
    ;

    private final String remark;
}
