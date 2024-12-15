package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 收货状态
 *
 * @author weiwenxin
 * @date 2022/11/3 10:06
 */
@Getter
@AllArgsConstructor
public enum ReceiptOrderStatus implements IRemark {
    // 收货状态:WAIT_DELIVER("待发货"),WAIT_RECEIVE(待收货),RECEIPTED(已收货),CANCEL("已取消"),
    WAIT_DELIVER("待发货"),
    WAIT_RECEIVE("待收货"),
    RECEIPTED("已收货"),
    CANCEL("已取消"),
    ;
    private final String remark;

    public ReceiptOrderStatus toReceipted() {
        if (WAIT_RECEIVE != this) {
            throw new ParamIllegalException("当前收货单状态不处于【{}】,无法进行收货操作，请刷新后重试！",
                    WAIT_RECEIVE.getRemark());
        }
        return RECEIPTED;
    }

    public void verifyReceipted() {
        if (RECEIPTED != this) {
            throw new ParamIllegalException("原料收货单状态处于【{}】才能进行操作，请刷新后重试！",
                    RECEIPTED.getRemark());
        }
    }

    public ReceiptOrderStatus toWaitReceive() {
        if (WAIT_DELIVER != this && WAIT_RECEIVE != this) {
            throw new ParamIllegalException("当前收货单状态不处于【{}】、【{}】，状态更新失败",
                    WAIT_DELIVER.getRemark(), WAIT_RECEIVE.getRemark());
        }
        return WAIT_RECEIVE;
    }
}
