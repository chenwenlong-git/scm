package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@RequiredArgsConstructor
@Getter
public enum QcResult implements IRemark {
    // 质检结果
    PASSED("合格"),
    FEW_NOT_PASSED("部分不合格"),
    NOT_PASSED("不合格");

    private final String remark;
}
