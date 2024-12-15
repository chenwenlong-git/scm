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
public enum ProcessSettleStatus implements IRemark {
    //加工结算单状态
    WAIT_SETTLE("待核算"),
    SETTLE_WAIT_EXAMINE("结算待审核"),
    AUDITED("已审核");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public ProcessSettleStatus toExamine() {
        if (WAIT_SETTLE != this) {
            throw new BizException("状态处于{}时才进行操作", WAIT_SETTLE.getRemark());
        }
        return SETTLE_WAIT_EXAMINE;
    }

    public ProcessSettleStatus toAudited() {
        if (SETTLE_WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", SETTLE_WAIT_EXAMINE.getRemark());
        }
        return AUDITED;
    }

    public ProcessSettleStatus toNotAudited() {
        if (SETTLE_WAIT_EXAMINE != this) {
            throw new BizException("状态处于{}时才进行操作", SETTLE_WAIT_EXAMINE.getRemark());
        }
        return WAIT_SETTLE;
    }

}
