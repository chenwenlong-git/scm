package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/1
 */
@Getter
@AllArgsConstructor
public enum PurchaseParentOrderStatus implements IRemark {
    // 采购母单状态
    WAIT_ORDER("待提交"),
    WAIT_APPROVE("待审核"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    DELETED("已取消"),
    ;
    private final String remark;


    public static PurchaseParentOrderStatus convertPurchaseStatus(PurchaseOrderStatus purchaseOrderStatus) {
        if (PurchaseOrderStatus.WAIT_APPROVE.equals(purchaseOrderStatus)) {
            return PurchaseParentOrderStatus.WAIT_APPROVE;
        }

        if (purchaseOrderStatus.getSort() >= PurchaseOrderStatus.WAIT_CONFIRM.getSort()
                && purchaseOrderStatus.getSort() <= PurchaseOrderStatus.WAIT_WAREHOUSING.getSort()) {
            return PurchaseParentOrderStatus.IN_PROGRESS;
        }

        if (purchaseOrderStatus.getSort() >= PurchaseOrderStatus.WAREHOUSED.getSort()) {
            return PurchaseParentOrderStatus.COMPLETED;
        }

        throw new BizException("错误的状态转换，请联系系统管理员");
    }

    /**
     * 未交数量排除统计的状态
     *
     * @return
     */
    public static List<PurchaseParentOrderStatus> getUndeliveredRidStatusList() {
        return Arrays.asList(WAIT_ORDER, WAIT_APPROVE, COMPLETED, DELETED);
    }

    public static List<PurchaseParentOrderStatus> cancelStatusList() {
        return Arrays.asList(WAIT_ORDER, WAIT_APPROVE, IN_PROGRESS);
    }

    public PurchaseParentOrderStatus toWaitApprove() {
        if (WAIT_ORDER != this) {
            throw new ParamIllegalException("当前采购需求单不处于【{}】，无法变更为【{}】，请刷新后重试！",
                    WAIT_ORDER.getRemark(), WAIT_APPROVE.getRemark());
        }
        return WAIT_APPROVE;
    }

    public PurchaseParentOrderStatus toInProgress() {
        if (WAIT_APPROVE != this) {
            throw new ParamIllegalException("当前采购需求单不处于【{}】，无法变更为【{}】，请刷新后重试！",
                    WAIT_APPROVE.getRemark(), IN_PROGRESS.getRemark());
        }
        return IN_PROGRESS;
    }

    /**
     * 只有待审核不通过才可以到待下单
     *
     * @return
     */
    public PurchaseParentOrderStatus toRefuseApprove() {
        if (WAIT_APPROVE != this) {
            throw new ParamIllegalException("当前采购需求单不处于【{}】，请刷新后重试！",
                    WAIT_APPROVE.getRemark());
        }
        return WAIT_ORDER;
    }


    public PurchaseParentOrderStatus toCompleted() {
        if (IN_PROGRESS != this) {
            throw new ParamIllegalException("当前采购需求单不处于【{}】，请刷新后重试！",
                    IN_PROGRESS.getRemark());
        }
        return COMPLETED;
    }
}
