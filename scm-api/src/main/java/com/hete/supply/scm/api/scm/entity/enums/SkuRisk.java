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
public enum SkuRisk implements IRemark {
    // 高风险
    HIGH("高风险"),
    // 中风险
    MIDDLE("中风险"),
    // 低风险
    LOW("低风险"),
    ;
    private final String remark;
}
