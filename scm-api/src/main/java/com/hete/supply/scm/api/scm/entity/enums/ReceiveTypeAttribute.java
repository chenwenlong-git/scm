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
public enum ReceiveTypeAttribute implements IRemark {
    // 收货单类型子类
    PROCESS_PRODUCT_REPAIR("加工成品入库-退货返修");
    private final String remark;
}
