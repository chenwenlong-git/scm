package com.hete.supply.scm.server.scm.qc.enums;

import com.hete.support.api.constant.IRemark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@RequiredArgsConstructor
@Getter
public enum QcOperate implements IRemark {
    // 质检操作
    PASSED("整单合格"),
    NOT_PASSED("整单不合格"),
    COMPLETED("质检完成"),
    ;


    private final String remark;
}
