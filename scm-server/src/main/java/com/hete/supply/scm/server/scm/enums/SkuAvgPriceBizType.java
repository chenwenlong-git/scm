package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum SkuAvgPriceBizType implements IRemark {
    // sku均价业务类型
    PURCHASE("采购"),
    DEVELOP_SAMPLE_ORDER("开发样品单"),
    PROCESS_ORDER("加工单");

    private final String remark;
}
