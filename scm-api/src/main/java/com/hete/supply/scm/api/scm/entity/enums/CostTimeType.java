package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/2/20 16:11
 */
@Getter
@AllArgsConstructor
public enum CostTimeType implements IRemark {
    // 成本时间维度
    DAY("每日"),
    MONTH("每月"),
    ;
    private final String remark;
}
