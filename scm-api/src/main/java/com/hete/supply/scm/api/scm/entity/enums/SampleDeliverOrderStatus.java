package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:55
 */
@Getter
@AllArgsConstructor
public enum SampleDeliverOrderStatus implements IRemark {
    // 样品发货单状态
    WAIT_RECEIVED_SAMPLE("待收样"),
    RECEIVED_SAMPLE("已收样"),
    CANCELED("已取消"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public SampleDeliverOrderStatus toCanceled() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品发货单不处于【{}】状态，无法取消该发货单，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.getRemark());
        }
        return CANCELED;
    }

    public SampleDeliverOrderStatus toReceivedSample() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品发货单不处于【{}】状态，无法确认收货，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.getRemark());
        }
        return RECEIVED_SAMPLE;
    }
}
