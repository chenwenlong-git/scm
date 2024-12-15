package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum FinanceRecoOrderStatus implements IRemark {

    // 财务对账单状态
    PENDING_ORDER("待收单"),
    WAIT_SUBMIT("待跟单提交"),
    WAIT_SUPPLIER_CONFIRM("待工厂确认"),
    WAIT_FOLLOW_CONFIRM("待跟单确认"),
    LAUNCH_APPROVE("发起审批"),
    UNDER_REVIEW("审批中"),
    COMPLETE("对账完成"),
    DELETE("已作废"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }

    /**
     * 验证状态是否支持转发
     *
     * @param financeRecoOrderStatus:
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/5/21 17:24
     */
    public static Boolean verifyTransfer(FinanceRecoOrderStatus financeRecoOrderStatus) {
        return (COMPLETE == financeRecoOrderStatus || DELETE == financeRecoOrderStatus);
    }

    public FinanceRecoOrderStatus toDelete() {
        if (this != WAIT_SUBMIT) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_SUBMIT.getRemark());
        }
        return FinanceRecoOrderStatus.DELETE;
    }

    public FinanceRecoOrderStatus toWaitSupplierConfirm() {
        if (this != WAIT_SUBMIT) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_SUBMIT.getRemark());
        }
        return FinanceRecoOrderStatus.WAIT_SUPPLIER_CONFIRM;
    }

    public FinanceRecoOrderStatus toWaitFollowConfirm() {
        if (this != WAIT_SUPPLIER_CONFIRM) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_SUPPLIER_CONFIRM.getRemark());
        }
        return FinanceRecoOrderStatus.WAIT_FOLLOW_CONFIRM;
    }

    public FinanceRecoOrderStatus toRejectWaitSubmit() {
        if (this != WAIT_SUPPLIER_CONFIRM) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_SUPPLIER_CONFIRM.getRemark());
        }
        return FinanceRecoOrderStatus.WAIT_SUBMIT;
    }

    public FinanceRecoOrderStatus toLaunchApprove() {
        if (this != WAIT_FOLLOW_CONFIRM) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_FOLLOW_CONFIRM.getRemark());
        }
        return FinanceRecoOrderStatus.LAUNCH_APPROVE;
    }

    public FinanceRecoOrderStatus toUnderReview() {
        if (!(this == WAIT_FOLLOW_CONFIRM || this == LAUNCH_APPROVE || this == UNDER_REVIEW)) {
            throw new BizException("当前对账单不处于【{}】、【{}】、【{}】状态，请刷新后重试", WAIT_FOLLOW_CONFIRM.getRemark(),
                    LAUNCH_APPROVE.getRemark(),
                    UNDER_REVIEW.getRemark());
        }
        return FinanceRecoOrderStatus.UNDER_REVIEW;
    }

    public FinanceRecoOrderStatus toRejectUnderReview() {
        if (this != WAIT_FOLLOW_CONFIRM) {
            throw new ParamIllegalException("当前对账单不处于【{}】状态，请刷新后重试", WAIT_FOLLOW_CONFIRM.getRemark());
        }
        return FinanceRecoOrderStatus.WAIT_SUBMIT;
    }

    public FinanceRecoOrderStatus toWaitSubmit() {
        if (this != UNDER_REVIEW) {
            return null;
        }
        return FinanceRecoOrderStatus.WAIT_SUBMIT;
    }

    public FinanceRecoOrderStatus toComplete() {
        if (this != UNDER_REVIEW) {
            return null;
        }
        return FinanceRecoOrderStatus.COMPLETE;
    }

    public static List<FinanceRecoOrderStatus> todoListStatus() {
        return Arrays.asList(PENDING_ORDER, WAIT_SUBMIT, WAIT_FOLLOW_CONFIRM, LAUNCH_APPROVE, UNDER_REVIEW);
    }

    public static List<FinanceRecoOrderStatus> todoSupplierListStatus() {
        return List.of(WAIT_SUPPLIER_CONFIRM);
    }

}
