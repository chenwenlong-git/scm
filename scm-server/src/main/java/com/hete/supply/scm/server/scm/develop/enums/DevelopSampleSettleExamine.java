package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:47
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleSettleExamine implements IRemark {
    //结算单审核
    ALREADY_CONFIRM("已对账"),
    SETTLE_AGREE("确认通过"),
    SETTLE_REFUSE("确认拒绝"),
    EXAMINE_AGREE("审核通过"),
    EXAMINE_REFUSE("审核拒绝");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
