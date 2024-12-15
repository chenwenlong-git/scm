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
public enum QcApproveOperate implements IRemark {
    // 审核
    PASSED("审核通过"),
    NOT_PASSED("审核不通过"),
    ;


    private final String remark;
}
