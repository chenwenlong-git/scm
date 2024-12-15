package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum StockUpOrderStatus implements IRemark {
    // 备货单状态
    TO_BE_FOLLOW_CONFIRM("待跟单确认"),
    TO_BE_ACCEPT("待接单"),
    IN_PROGRESS("进行中"),
    FINISH("已完结"),
    CANCEL("已取消"),
    ;

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

    public StockUpOrderStatus toBeAccept() {
        if (TO_BE_FOLLOW_CONFIRM != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return TO_BE_ACCEPT;
    }

    public StockUpOrderStatus toFinish() {
        if (IN_PROGRESS != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return FINISH;
    }

    public StockUpOrderStatus toCancel() {
        if (TO_BE_FOLLOW_CONFIRM != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return CANCEL;
    }

    public StockUpOrderStatus toInProcess() {
        if (TO_BE_ACCEPT != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return IN_PROGRESS;
    }

    public StockUpOrderStatus toBeFollowConfirm() {
        if (TO_BE_ACCEPT != this) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        return TO_BE_FOLLOW_CONFIRM;
    }
}
