package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 17:34
 */
@Getter
@AllArgsConstructor
public enum DevelopChildOrderStatus implements IRemark {
    // 开发子单状态
    PAMPHLET("打版", 1, DevelopPamphletOrderStatus.getValues()),
    REVIEW("审版", 2, DevelopReviewOrderStatus.getValues()),
    PRICING("核价", 3, DevelopPricingOrderStatus.getValues()),
    NEWEST("上新", 4, null),
    COMPLETE("完成", 5, null),
    CANCEL("取消开发", 6, null),
    ;

    private final String remark;
    private final Integer sort;
    private final List<? extends Enum<?>> relatedEnums;

    public void toReview() {
        if (PAMPHLET != this) {
            throw new ParamIllegalException("开发子单状态处于{}时才进行操作，请刷新页面后重试！", PAMPHLET.getRemark());
        }
    }

    public DevelopChildOrderStatus toPricing() {
        if (REVIEW != this) {
            throw new ParamIllegalException();
        }
        return PRICING;
    }

    public DevelopChildOrderStatus toNewest() {
        if (PRICING != this) {
            throw new ParamIllegalException("开发子单状态处于{}时才进行操作，请刷新页面后重试！", PRICING.getRemark());
        }
        return NEWEST;
    }

    public DevelopChildOrderStatus toComplete() {
        if (NEWEST != this) {
            throw new ParamIllegalException("开发子单状态处于{}时才进行操作，请刷新页面后重试！", NEWEST.getRemark());
        }
        return COMPLETE;
    }

    /**
     * 提供PLM
     *
     * @param statusList:
     * @return DevelopChildOrderStatus
     * @author ChenWenLong
     * @date 2023/9/11 15:28
     */
    public static DevelopChildOrderStatus getLatestStatus(List<DevelopChildOrderStatus> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            throw new BizException("错误的状态列表，无法获取最晚状态！");
        }
        DevelopChildOrderStatus developChildOrderStatus = statusList.get(0);

        for (DevelopChildOrderStatus status : statusList) {
            if (status.getSort() < developChildOrderStatus.getSort()) {
                developChildOrderStatus = status;
            }
        }
        return developChildOrderStatus;
    }

    /**
     * 提供PLM
     *
     * @param status:
     * @return DevelopChildOrderStatus
     * @author ChenWenLong
     * @date 2023/9/11 15:28
     */
    public static DevelopChildOrderStatus getNextStatus(DevelopChildOrderStatus status) {
        if (status == null) {
            throw new BizException("状态错误，请传入相关开发子单状态");
        }
        if (DevelopChildOrderStatus.COMPLETE.equals(status)) {
            return status;
        }
        DevelopChildOrderStatus[] sortedValues = DevelopChildOrderStatus.values();
        Arrays.sort(sortedValues, Comparator.comparing(DevelopChildOrderStatus::getSort));
        int currentIndex = Arrays.binarySearch(sortedValues, status);
        int nextIndex = (currentIndex + 1) % sortedValues.length;
        // 如果当前状态是最后一个枚举，则返回最后一个枚举
        if (status == sortedValues[sortedValues.length - 1]) {
            return status;
        }
        return sortedValues[nextIndex];

    }

    /**
     * 取消开发判断规则
     *
     * @param isSample:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/22 10:17
     */
    public void cancel(BooleanType isSample) {
        // 需要打样判断规则
        if (BooleanType.TRUE.equals(isSample)) {
            if (!(PAMPHLET == this || NEWEST == this)) {
                throw new ParamIllegalException("开发子单处于{}或{}状态，进行操作！", PAMPHLET.getRemark(),
                        NEWEST.getRemark());
            }
        }

        // 不需要打样判断规则
        if (null == isSample || BooleanType.FALSE.equals(isSample)) {
            if (!(PRICING == this || NEWEST == this)) {
                throw new ParamIllegalException("开发子单处于{}或{}状态，进行操作！", PRICING.getRemark(),
                        NEWEST.getRemark());
            }
        }
    }
}
