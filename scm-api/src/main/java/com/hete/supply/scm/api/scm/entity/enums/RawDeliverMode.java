package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/6/7 13:55
 */
@RequiredArgsConstructor
@Getter
public enum RawDeliverMode implements IRemark {
    // 原料出库方式
    SUPPLY_RAW("补充原料出库"),
    RECEIVE_RAW_DELIVER("接单原料出库"),
    ;


    private final String remark;
}
