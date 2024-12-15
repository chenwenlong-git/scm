package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderStatus implements IRemark {

    /**
     * 缺货
     */
    LACK("缺货", 1),

    /**
     * 待齐备
     */
    WAIT_READY("待齐备", 2),

    /**
     * 待下单
     */
    WAIT_ORDER("待下单", 3),

    /**
     * 待排产
     */
    WAIT_PLAN("待排产", 4),

    /**
     * 待投产
     */
    WAIT_PRODUCE("待投产", 5),

    /**
     * 已投产
     */
    PRODUCED("已投产", 6),

    /**
     * 加工中
     */
    PROCESSING("加工中", 7),


    /**
     * 完工待交接
     */
    WAIT_MOVING("完工待交接", 8),

    /**
     * 后整质检中
     */
    CHECKING("后整质检中", 9),

    /**
     * 待发货
     */
    WAIT_DELIVERY("待发货", 10),

    /**
     * 待收货
     */
    WAIT_RECEIPT("待收货", 11),

    /**
     * 待入库
     */
    WAIT_STORE("待入库", 12),

    /**
     * 已完结
     */
    STORED("已完结", 13),

    /**
     * 已作废
     */
    DELETED("已作废", 14),

    /**
     * 返工中
     */
    REWORKING("返工中", 15),


    ;


    /**
     * 描述
     */
    private final String desc;
    private final Integer sort;

    @Override
    public String getRemark() {
        return this.desc;
    }

}
