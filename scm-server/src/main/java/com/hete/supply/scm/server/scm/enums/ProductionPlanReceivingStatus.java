package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排产计划接货状态枚举
 *
 * @author yanjiawei
 * Created on 2023/8/29.
 */
@Getter
@AllArgsConstructor
public enum ProductionPlanReceivingStatus implements IRemark {

    /**
     * 未接货
     */
    NOT_RECEIVED("未接货"),

    /**
     * 已接货
     */
    RECEIVED("已接货");

    private final String remark;
}
