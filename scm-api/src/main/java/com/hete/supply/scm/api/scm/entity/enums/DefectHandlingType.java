package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum DefectHandlingType implements IRemark {
    // 次品类型
    BULK_DEFECT("质检不合格"),
    PROCESS_DEFECT("加工次品"),
    INSIDE_DEFECT("库内抽查"),
    MATERIAL_DEFECT("原料次品"),
    REPAIR("返修货"),
    ;

    private final String remark;
}
