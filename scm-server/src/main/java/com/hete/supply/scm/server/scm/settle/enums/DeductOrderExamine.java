package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.support.api.constant.IRemark;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum DeductOrderExamine implements IRemark {
    //扣款单审核
    PRICE_CONFIRM_AGREE("价格确认通过"),
    PRICE_CONFIRM_REFUSE("价格确认拒绝"),
    ALREADY_CONFIRM("已提交"),
    CONFIRM_AGREE("确认通过"),
    CONFIRM_REFUSE("确认拒绝"),
    EXAMINE_AGREE("审核通过"),
    EXAMINE_REFUSE("审核拒绝"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public static DeductOrderExamine converterType(DeductStatus deductStatus,
                                                   DeductType deductType,
                                                   BooleanType examine) {
        // 价差扣款时待提交审核通过
        if (DeductStatus.WAIT_SUBMIT.equals(deductStatus)
                && DeductType.PRICE.equals(deductType)
                && BooleanType.TRUE.equals(examine)) {
            return ALREADY_CONFIRM;
            // 非价差扣款时提交
        } else if (DeductStatus.WAIT_SUBMIT.equals(deductStatus)
                && !DeductType.PRICE.equals(deductType)
                && BooleanType.TRUE.equals(examine)) {
            return ALREADY_CONFIRM;
            // 价差扣款时价格确认同意
        } else if (DeductStatus.WAIT_PRICE.equals(deductStatus)
                && DeductType.PRICE.equals(deductType)
                && BooleanType.TRUE.equals(examine)) {
            return PRICE_CONFIRM_AGREE;
            // 价差扣款时价格确认拒绝
        } else if (DeductStatus.WAIT_PRICE.equals(deductStatus)
                && DeductType.PRICE.equals(deductType)
                && BooleanType.FALSE.equals(examine)) {
            return PRICE_CONFIRM_REFUSE;
            // 供应商确认同意
        } else if (DeductStatus.WAIT_CONFIRM.equals(deductStatus)
                && BooleanType.TRUE.equals(examine)) {
            return CONFIRM_AGREE;
            // 供应商确认拒绝
        } else if (DeductStatus.WAIT_CONFIRM.equals(deductStatus)
                && BooleanType.FALSE.equals(examine)) {
            return CONFIRM_REFUSE;
            // 审批同意
        } else if (DeductStatus.WAIT_EXAMINE.equals(deductStatus)
                && BooleanType.TRUE.equals(examine)) {
            return EXAMINE_AGREE;
            // 审批拒绝
        } else if (DeductStatus.WAIT_EXAMINE.equals(deductStatus)
                && BooleanType.FALSE.equals(examine)) {
            return EXAMINE_REFUSE;
        } else {
            throw new BizException("扣款单的审批类型转换失败，请联系管理员！");
        }
    }
}
