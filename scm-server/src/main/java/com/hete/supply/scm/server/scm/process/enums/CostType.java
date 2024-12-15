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
public enum CostType implements IRemark {
    // 成本类别
    RAW_MATERIAL("原料成本"),
    PROCESS_SCAN_MANPOWER("加工工序扫码人力成本"),
    REPAIR_MANPOWER("返修人力成本"),
    FIXED_LOSS("固定损耗成本"),
    REWORK("返工成本")
    // 其他成本类别...
    ;

    private final String remark;
}

