package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Getter
@AllArgsConstructor
public enum DeliveryTypeAttribute implements IRemark {
    // 发货单类型子分类
    PROCESS_REPAIR("加工单出库-退货返修"),
    NORMAL_PROCESS("加工单出库-普通加工");
    private final String remark;
}
