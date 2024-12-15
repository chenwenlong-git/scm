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
public enum DevelopSampleQuality implements IRemark {
    // 样品需求:
    QUALIFIED("合格"),
    UNQUALIFIED("不合格"),
    ;

    private final String remark;


}
