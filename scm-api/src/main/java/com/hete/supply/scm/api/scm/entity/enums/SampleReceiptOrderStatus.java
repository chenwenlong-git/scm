package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/2 23:14
 */
@Getter
@AllArgsConstructor
public enum SampleReceiptOrderStatus implements IRemark {
    // 样品收货单状态
    WAIT_RECEIVED_SAMPLE("待收样"),
    RECEIVED_SAMPLE("已收样"),
    CANCELED("已取消"),
    ;
    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


    public SampleReceiptOrderStatus toCanceled() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品发货单不处于【{}】，无法取消该发货单，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.getRemark());
        }
        return CANCELED;
    }

    public SampleReceiptOrderStatus toReceivedSample() {
        if (WAIT_RECEIVED_SAMPLE != this) {
            throw new ParamIllegalException("当前样品发货单不处于【{}】，无法确认收货，请刷新后重试！",
                    WAIT_RECEIVED_SAMPLE.toReceivedSample());
        }
        return RECEIVED_SAMPLE;
    }
}
