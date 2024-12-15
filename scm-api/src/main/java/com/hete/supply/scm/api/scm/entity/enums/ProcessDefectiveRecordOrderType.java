package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/06/05 09:02
 */
@AllArgsConstructor
@Getter
public enum ProcessDefectiveRecordOrderType implements IRemark {

    /**
     * 收货单
     */
    RECEIVE_ORDER("收货单"),

    /**
     * 加工单
     */
    PROCESS_ORDER("加工单"),

    /**
     * 退货单
     */
    RETURN_ORDER("退货单"),

    ;
    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
