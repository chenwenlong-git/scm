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
public enum DevelopSampleDemand implements IRemark {
    // 样品需求:
    SATISFY("满足"),
    DISSATISFIED("不满足"),
    NEED_NOT_CONFIRMATION("无需确认"),
    ;

    private final String remark;


}
