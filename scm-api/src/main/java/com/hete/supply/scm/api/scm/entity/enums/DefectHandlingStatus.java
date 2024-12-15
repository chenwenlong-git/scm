package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum DefectHandlingStatus implements IRemark {
    // 次品处理状态:WAIT_CONFIRM(待确认),CONFIRMED(已确认),CONFIRMED_FAIL(处理失败),
    WAIT_CONFIRM("待确认"),
    CONFIRMED("已确认"),
    CONFIRMED_FAIL("处理失败"),
    ;

    private final String remark;

    public DefectHandlingStatus toFailStatus() {
        if (CONFIRMED != this) {
            throw new BizException("次品处理不处于可更新状态");
        }
        return CONFIRMED_FAIL;
    }

    public DefectHandlingStatus toConfirmed() {
        if (WAIT_CONFIRM != this && CONFIRMED_FAIL != this) {
            throw new BizException("次品处理不处于可操作状态");
        }
        return CONFIRMED;
    }
}
