package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum DeductStatus implements IRemark {
    //扣款状态
    WAIT_SUBMIT("待提交"),
    WAIT_PRICE("价格确认"),
    WAIT_CONFIRM("待确认"),
    WAIT_EXAMINE("待审核"),
    AUDITED("已审核"),
    REFUSED("已拒绝"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public DeductStatus toConfirm() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_CONFIRM;
    }

    public DeductStatus processToWaitExamine() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_EXAMINE;
    }

    public DeductStatus toExamine() {
        if (WAIT_CONFIRM != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_CONFIRM.getRemark());
        }
        return WAIT_EXAMINE;
    }

    public DeductStatus toNotExamine() {
        if (WAIT_CONFIRM != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_CONFIRM.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public DeductStatus processToNotExamine() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public DeductStatus toAudited() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return AUDITED;
    }

    public DeductStatus toNotAudited() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return WAIT_CONFIRM;
    }

    public DeductStatus toNotAuditedReturn() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return REFUSED;
    }

    public DeductStatus toWaitConfirm() {
        if (WAIT_PRICE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_PRICE.getRemark());
        }
        return WAIT_CONFIRM;

    }

    public DeductStatus toNotWaitConfirm() {
        if (WAIT_PRICE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_PRICE.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public DeductStatus priceToWaitExamine() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_PRICE;
    }
}
