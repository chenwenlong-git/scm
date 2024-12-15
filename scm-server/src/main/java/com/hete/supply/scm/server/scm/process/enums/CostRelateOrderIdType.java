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
public enum CostRelateOrderIdType implements IRemark {
    // 关联主键单号表名
    PROCESS_ORDER_ITEM("process_order_item"),
    REPAIR_ORDER_ITEM("repair_order_item");

    private final String remark;
}
