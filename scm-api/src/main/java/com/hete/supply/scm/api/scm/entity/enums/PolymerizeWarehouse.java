package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/2/20 15:45
 */
@Getter
@AllArgsConstructor
public enum PolymerizeWarehouse implements IRemark {
    // 仓库聚合维度
    GUANGZHOU("广州仓"),
    NON_WH("非网红仓"),
    WH("网红仓"),
    VIRTUAL_WAREHOUSE("工厂虚拟仓"),
    FOREIGN_THIRD_PARTY("海外三方仓"),
    FOREIGN_SELF_RUN("海外自营仓"),
    ;
    private final String remark;
}
