package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 17:34
 */
@Getter
@AllArgsConstructor
public enum DevelopReviewOrderStatus implements IRemark {
    // 审版状态
    TO_BE_SUBMITTED_REVIEW("待提交审版"),
    WAIT_REVIEW("待审版"),
    REVIEWING("审版中"),
    COMPLETED_REVIEW("审版完成"),
    CANCEL_REVIEW("取消审版"),
    ;

    private final String remark;

    public static List<DevelopReviewOrderStatus> getValues() {
        return Arrays.asList(DevelopReviewOrderStatus.values());
    }

    public static List<DevelopReviewOrderStatus> developReviewMenuList() {
        return Arrays.asList(TO_BE_SUBMITTED_REVIEW, WAIT_REVIEW, REVIEWING, COMPLETED_REVIEW);
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    public DevelopReviewOrderStatus toCompletedReview() {
        if (!(REVIEWING == this || WAIT_REVIEW == this)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return COMPLETED_REVIEW;
    }

    public DevelopReviewOrderStatus toReviewing() {
        if (WAIT_REVIEW != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return REVIEWING;
    }

    public DevelopReviewOrderStatus toWaitReview() {
        if (TO_BE_SUBMITTED_REVIEW != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return WAIT_REVIEW;
    }

    public static DevelopReviewOrderStatus isEnums(String status) {
        for (DevelopReviewOrderStatus value : DevelopReviewOrderStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

    public DevelopReviewOrderStatus waitReviewToReviewing() {
        if (!(REVIEWING == this || WAIT_REVIEW == this)) {
            throw new ParamIllegalException("审版单状态处于{}或{}才可以进行操作，请刷新页面后重试！",
                    DevelopReviewOrderStatus.WAIT_REVIEW.getRemark(),
                    DevelopReviewOrderStatus.REVIEWING.getRemark());
        }
        return REVIEWING;
    }

}
