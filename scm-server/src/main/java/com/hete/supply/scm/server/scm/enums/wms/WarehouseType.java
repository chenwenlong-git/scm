package com.hete.supply.scm.server.scm.enums.wms;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/28 11:21
 */
@Getter
@AllArgsConstructor
public enum WarehouseType implements IRemark {
    // 仓库类型
    DOMESTIC_SELF_RUN("国内自营", "DOMESTIC_SELF_RUN"),
    DOMESTIC_THIRD_PARTY("国内三方", "DOMESTIC_THIRD_PARTY"),
    FOREIGN_SELF_RUN("海外自营", "FOREIGN_SELF_RUN"),
    FOREIGN_THIRD_PARTY("海外三方", "FOREIGN_THIRD_PARTY"),
    MACHINING_WAREHOUSE("加工仓", "MACHINING_WAREHOUSE"),
    ;


    private final String remark;

    private final String name;
}
