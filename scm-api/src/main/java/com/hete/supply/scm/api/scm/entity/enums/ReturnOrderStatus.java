package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退货状态
 *
 * @author rockyHuas
 * @date 2023/06/27 10:06
 */
@Getter
@AllArgsConstructor
public enum ReturnOrderStatus implements IRemark {
    // 收货状态:WAIT_MOVING(待交接),WAIT_HANDLE(待处理),WAIT_RECEIVE(待收货),RECEIPTED(已收货),LOST(已丢失),
    WAIT_MOVING("待交接"),
    WAIT_HANDLE("待处理"),
    WAIT_RECEIVE("待收货"),
    RECEIPTED("已收货"),
    LOST("已丢失"),
    ;
    private final String remark;

    public ReturnOrderStatus toReceipted() {
        if (WAIT_RECEIVE != this) {
            throw new ParamIllegalException("当前退货单状态不处于【{}】,无法进行收货操作，请刷新后重试！",
                    WAIT_RECEIVE.getRemark());
        }
        return RECEIPTED;
    }
}
