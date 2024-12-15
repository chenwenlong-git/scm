package com.hete.supply.scm.server.scm.adjust.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/6/13 14:42
 */
@Getter
@AllArgsConstructor
public enum ApproveStatus implements IRemark {
    // 审批状态:TO_BE_APPROVE(待审批),APPROVING(审批中),APPROVE_PASSED(审批通过),APPROVE_REJECT(审批拒绝),
    TO_BE_APPROVE("待审批"),
    APPROVING("审批中"),
    APPROVE_PASSED("审批通过"),
    APPROVE_REJECT("审批拒绝"),
    FAIL("审批流程异常"),
    ;

    private final String remark;

    public ApproveStatus toApproving() {
        if (this != TO_BE_APPROVE) {
            return null;
        }
        return APPROVING;
    }

    public ApproveStatus toPassed() {
        if (this != APPROVING) {
            return null;
        }
        return APPROVE_PASSED;
    }

    public ApproveStatus toReject() {
        if (this != APPROVING) {
            return null;
        }
        return APPROVE_REJECT;
    }

    public ApproveStatus toFail() {
        if (this != APPROVING) {
            return null;
        }
        return FAIL;
    }
}
