package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@AllArgsConstructor
@Getter
public enum RepairOrderStatus implements IRemark {

    // 返修单状态枚举
    WAITING_FOR_READY("待齐备"),
    WAITING_FOR_PRODUCTION("待投产"),
    IN_PROCESS("加工中"),
    WAITING_FOR_QUALITY_INSPECTION("待质检"),
    WAITING_FOR_RECEIVING("待收货"),
    COMPLETED("已完成"),
    CANCELED("已取消");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}

