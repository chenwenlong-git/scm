package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:33
 */
@Getter
@AllArgsConstructor
public enum PrepaymentOrderStatus implements IRemark {
    // 预付款状态:TO_BE_FOLLOW_SUBMIT(待跟单提交),APPROVING(审批中),TO_BE_PAYMENT(待付款),PARTIAL_PAYMENT(部分付款),FULL_PAYMENT(全部付款),DELETED(作废),
    TO_BE_FOLLOW_SUBMIT("待跟单提交"),
    LAUNCH_APPROVE("发起审批"),
    APPROVING("审批中"),
    TO_BE_PAYMENT("待付款"),
    PARTIAL_PAYMENT("部分付款"),
    FULL_PAYMENT("预付款完成"),
    DELETED("已作废"),
    ;

    private final String remark;

    public PrepaymentOrderStatus toLaunchApprove() {
        if (this != TO_BE_FOLLOW_SUBMIT) {
            throw new ParamIllegalException("当前预付款单不处于【{}】状态，请刷新后重试", TO_BE_FOLLOW_SUBMIT.getRemark());
        }
        return PrepaymentOrderStatus.LAUNCH_APPROVE;
    }

    public PrepaymentOrderStatus rejectedToFollowSubmit() {
        if (this != APPROVING) {
            return null;
        }
        return PrepaymentOrderStatus.TO_BE_FOLLOW_SUBMIT;
    }

    public PrepaymentOrderStatus toFullPayment() {
        if (this != TO_BE_PAYMENT && this != PARTIAL_PAYMENT) {
            throw new ParamIllegalException("当前预付款单不处于【{}】、【{}】状态，请刷新后重试",
                    TO_BE_PAYMENT.getRemark(), PARTIAL_PAYMENT.getRemark());
        }
        return PrepaymentOrderStatus.FULL_PAYMENT;
    }

    public PrepaymentOrderStatus toManualFullPayment() {
        if (this != PARTIAL_PAYMENT) {
            throw new ParamIllegalException("当前预付款单不处于【{}】状态，请刷新后重试", PARTIAL_PAYMENT.getRemark());
        }
        return PrepaymentOrderStatus.FULL_PAYMENT;
    }

    public PrepaymentOrderStatus toPartialPayment() {
        if (this != TO_BE_PAYMENT && this != PARTIAL_PAYMENT) {
            throw new ParamIllegalException("当前预付款单不处于【{}】、【{}】状态，请刷新后重试",
                    TO_BE_PAYMENT.getRemark(), PARTIAL_PAYMENT.getRemark());
        }
        return PrepaymentOrderStatus.PARTIAL_PAYMENT;
    }

    public PrepaymentOrderStatus toBePayment() {
        if (this != APPROVING) {
            return null;
        }
        return PrepaymentOrderStatus.TO_BE_PAYMENT;
    }

    public PrepaymentOrderStatus toApproving() {
        if (this != LAUNCH_APPROVE && this != APPROVING) {
            throw new ParamIllegalException("当前预付款单不处于【{}】、【{}】状态，请刷新后重试",
                    LAUNCH_APPROVE.getRemark(), APPROVING.getRemark());
        }
        return PrepaymentOrderStatus.APPROVING;
    }

    public PrepaymentOrderStatus toDelete() {
        if (this != TO_BE_FOLLOW_SUBMIT) {
            throw new ParamIllegalException("当前预付款单不处于【{}】状态，请刷新后重试", TO_BE_FOLLOW_SUBMIT.getRemark());
        }
        return PrepaymentOrderStatus.DELETED;
    }

    /**
     * 不处于审批中的可转交状态
     *
     * @return
     */
    public static List<PrepaymentOrderStatus> transferStatus() {
        return Arrays.asList(TO_BE_FOLLOW_SUBMIT, LAUNCH_APPROVE, TO_BE_PAYMENT, PARTIAL_PAYMENT);
    }

    public static List<PrepaymentOrderStatus> canTransferStatus() {
        return Arrays.asList(TO_BE_FOLLOW_SUBMIT, LAUNCH_APPROVE, TO_BE_PAYMENT, PARTIAL_PAYMENT, APPROVING);
    }

    public static List<PrepaymentOrderStatus> todoListStatus() {
        return Arrays.asList(TO_BE_FOLLOW_SUBMIT, LAUNCH_APPROVE, APPROVING, TO_BE_PAYMENT, PARTIAL_PAYMENT);
    }

    public PrepaymentOrderStatus withdraw() {
        if (this != LAUNCH_APPROVE && this != APPROVING) {
            return null;
        }
        return PrepaymentOrderStatus.TO_BE_FOLLOW_SUBMIT;
    }
}
