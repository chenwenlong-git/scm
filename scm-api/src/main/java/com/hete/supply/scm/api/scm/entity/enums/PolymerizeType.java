package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/2/20 15:45
 */
@Getter
@AllArgsConstructor
public enum PolymerizeType implements IRemark {
    // 聚合类型：单仓、多仓
    SINGLE_WAREHOUSE("单仓维度"),
    MULTIPLE_WAREHOUSE("多仓维度"),
    ;
    private final String remark;
}
