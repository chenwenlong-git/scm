package com.hete.supply.scm.server.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/17 16:55
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum DefectRate implements IRemark {
    // 次品不良率枚举
    HIGH_DEFECT_RATE("不良率高"),
    LOW_DEFECT_RATE("质检数少/不良率低"),
    ALL_DEFECTS("整单不合格"),
    ;

    private String remark;

}
