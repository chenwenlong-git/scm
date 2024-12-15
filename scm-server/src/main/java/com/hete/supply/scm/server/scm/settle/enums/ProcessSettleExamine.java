package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum ProcessSettleExamine implements IRemark {
    //加工结算单审核
    ALREADY_SETTLE("确认核算"),
    EXAMINE_AGREE("审核通过"),
    EXAMINE_REFUSE("审核拒绝"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

}
