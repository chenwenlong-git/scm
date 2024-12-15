package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/30 19:37
 */
@Getter
@AllArgsConstructor
public enum DevelopReviewSampleSource implements IRemark {
    //审版单样品需求来源
    SELF_SAMPLE("自荐新款"),
    TWO_PROVIDE("二供"),
    OPTIMIZE_SAMPLE("优化样"),
    NORMAL_SAMPLE("非自荐新款");
    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
