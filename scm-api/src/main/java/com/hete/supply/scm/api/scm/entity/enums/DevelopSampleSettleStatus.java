package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/08/1 14:46
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleSettleStatus implements IRemark {
    //结算单状态
    WAIT_CONFIRM("待对账"),
    WAIT_SETTLE("供应商确认"),
    SETTLE_WAIT_EXAMINE("财务审核"),
    AUDITED("待支付"),
    PART_SETTLE("部分支付"),
    SETTLE("已支付");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public DevelopSampleSettleStatus toSettle() {
        if (WAIT_CONFIRM != this) {
            throw new BizException("状态处于{}时才进行操作，请刷新页面后重试！", WAIT_CONFIRM.getRemark());
        }
        return WAIT_SETTLE;
    }

    public DevelopSampleSettleStatus toExamine() {
        if (WAIT_SETTLE != this) {
            throw new BizException("状态处于{}时才进行操作，请刷新页面后重试！", WAIT_SETTLE.getRemark());
        }
        return SETTLE_WAIT_EXAMINE;
    }

    public DevelopSampleSettleStatus toNotExamine() {
        if (WAIT_SETTLE != this) {
            throw new BizException("状态处于{}时才进行操作，请刷新页面后重试！", WAIT_SETTLE.getRemark());
        }
        return WAIT_CONFIRM;
    }

    public DevelopSampleSettleStatus toAudited() {
        if (SETTLE_WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作，请刷新页面后重试！", SETTLE_WAIT_EXAMINE.getRemark());
        }
        return AUDITED;
    }

    public DevelopSampleSettleStatus toNotAudited() {
        if (SETTLE_WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作，请刷新页面后重试！", SETTLE_WAIT_EXAMINE.getRemark());
        }
        return WAIT_SETTLE;
    }

}
