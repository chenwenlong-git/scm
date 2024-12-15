package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Getter
@AllArgsConstructor
public enum CostRelatedOrderType implements IRemark {
    // 成本关联单号类型
    PROCESS_ORDER("加工单"),
    REPAIR_ORDER("返修单"),
    // 其他关联单号类型...
    ;

    private final String remark;
}
