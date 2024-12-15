package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/19 14:24
 */
@Getter
@AllArgsConstructor
public enum GradeType implements IRemark {
    // 职级类别
    COLORIST("染发师"),
    STYLIST("造型师"),
    CLIPS("缝卡子"),
    HEADGEAR("缝头套"),
    ;

    private final String remark;
}
