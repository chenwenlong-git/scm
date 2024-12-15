package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/6/7 14:46
 */
@Getter
@AllArgsConstructor
public enum ProcessSettleOrderType implements IRemark {
    //加工结算单关联单据类型：补款单(REPLENISH)、扣款单(DEDUCT)、扫码记录
    REPLENISH("补款单"),
    DEDUCT("扣款单"),
    SCAN("扫码记录");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
