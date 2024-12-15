package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplementStatus implements IRemark {
    //补款状态：待提交(WAIT_SUBMIT)、WAIT_PRICE(价格确认)、待确认(WAIT_CONFIRM)、待审核(WAIT_EXAMINE)、已审核(AUDITED)、已结算(SETTLE)',
    WAIT_SUBMIT("待提交"),
    WAIT_PRICE("价格确认"),
    WAIT_CONFIRM("待确认"),
    WAIT_EXAMINE("待审核"),
    AUDITED("已审核"),
    ;

    private final String name;

    public static List<SupplementStatus> getSupplierAllStatusList() {
        return Arrays.asList(WAIT_CONFIRM, WAIT_EXAMINE, AUDITED);
    }

    @Override
    public String getRemark() {
        return this.name;
    }


    public SupplementStatus toConfirm() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_CONFIRM;
    }


    public SupplementStatus processToWaitExamine() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_EXAMINE;
    }

    public SupplementStatus toExamine() {
        if (WAIT_CONFIRM != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_CONFIRM.getRemark());
        }
        return WAIT_EXAMINE;

    }

    public SupplementStatus toNotExamine() {
        if (WAIT_CONFIRM != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_CONFIRM.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public SupplementStatus processToNotAudited() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public SupplementStatus toAudited() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return AUDITED;
    }

    public SupplementStatus toNotAudited() {
        if (WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_EXAMINE.getRemark());
        }
        return WAIT_CONFIRM;
    }

    public SupplementStatus toWaitConfirm() {
        if (WAIT_PRICE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_PRICE.getRemark());
        }
        return WAIT_CONFIRM;

    }

    public SupplementStatus toNotWaitConfirm() {
        if (WAIT_PRICE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_PRICE.getRemark());
        }
        return WAIT_SUBMIT;
    }

    public SupplementStatus priceToWaitExamine() {
        if (WAIT_SUBMIT != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SUBMIT.getRemark());
        }
        return WAIT_PRICE;
    }
}
