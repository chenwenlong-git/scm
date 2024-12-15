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
public enum DevelopSampleStage implements IRemark {
    // 样品阶段
    SAMPLE("样品"),
    PRENATAL("产前样"),
    NORMAL_SAMPLE("正常货"),
    ;

    private final String remark;


}
