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
public enum DevelopReviewOrderType implements IRemark {
    // 审版类型
    SAMPLE_REVIEW("样品审版"),
    PRENATAL_REVIEW("产前样审版"),

    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}
