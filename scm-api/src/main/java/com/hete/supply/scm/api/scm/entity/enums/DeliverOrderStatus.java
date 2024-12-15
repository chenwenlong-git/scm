package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 11:55
 */
@Getter
@AllArgsConstructor
public enum DeliverOrderStatus implements IRemark {
    // 发货状态:
    WAIT_DELIVER("待发货", 1),
    WAIT_RECEIVE("待收货", 2),
    RECEIVING("收货中", 3),
    RECEIVED("已收货", 4),
    WAIT_QC("待质检", 5),
    WAIT_WAREHOUSING("待入库", 6),
    WAREHOUSED("已入库", 7),
    RETURN("已退货", 8),
    DELETED("取消发货", 9),
    SETTLE("已结算", 10),
    ;

    private final String name;
    private final Integer sort;

    public static List<DeliverOrderStatus> getInTransitStatusList() {
        return Arrays.asList(WAIT_RECEIVE, RECEIVING, RECEIVED, WAIT_QC, WAIT_WAREHOUSING);
    }

    public static DeliverOrderStatus getEarliestStatus(List<DeliverOrderStatus> statusList) {
        if (statusList == null || statusList.isEmpty()) {
            throw new BizException("错误的状态列表，无法获取最早状态。");
        }
        DeliverOrderStatus deliverOrderStatus = statusList.get(0);

        for (DeliverOrderStatus status : statusList) {
            if (status.getSort() < deliverOrderStatus.getSort()) {
                deliverOrderStatus = status;
            }
        }

        return deliverOrderStatus;
    }

    @Override
    public String getRemark() {
        return this.name;
    }

    public DeliverOrderStatus toWaitReceive() {
        if (this != WAIT_DELIVER) {
            throw new ParamIllegalException("当前发货单不处于【{}】状态，无法进行确认发货操作，请刷新后重试！"
                    , WAIT_DELIVER.getRemark());
        }

        return WAIT_RECEIVE;
    }

    public DeliverOrderStatus toDeleted() {
        if (this == WAREHOUSED || this == DELETED) {
            throw new ParamIllegalException("当前状态无法取消发货，请刷新后重试！");
        }

        return DELETED;
    }

    public DeliverOrderStatus toReceived() {
        if (this != WAIT_RECEIVE && this != RECEIVING) {
            throw new BizException("当前发货单不处于【{}】状态，无法进行收货操作，请刷新页面后重试！"
                    , WAIT_RECEIVE.getRemark());
        }

        return RECEIVED;
    }

    public DeliverOrderStatus toReceiving() {
        if (this != WAIT_RECEIVE) {
            throw new BizException("当前发货单不处于【{}】状态，无法进行收货操作，请刷新页面后重试！"
                    , WAIT_RECEIVE.getRemark());
        }

        return RECEIVING;
    }

    public DeliverOrderStatus toWarehoused() {
        if (this == WAREHOUSED) {
            throw new BizException("当前发货单已经处于【{}】状态，请刷新页面后重试！", WAREHOUSED.getRemark());
        }

        return WAREHOUSED;
    }
}
