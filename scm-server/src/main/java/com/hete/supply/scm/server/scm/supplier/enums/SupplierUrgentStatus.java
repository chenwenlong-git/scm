package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/07/18 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplierUrgentStatus implements IRemark {
    // 供应商加急状态
    URGENT("加急"),
    NOT_URGENT("不加急"),
    LOGISTICS_AGING("未获取供应商物流时效"),
    CYCLE("未获取到生产周期"),
    LOGISTICS_AGING_CYCLE("未获取供应商物流时效及生产周期");

    private final String name;


    @Override
    public String getRemark() {
        return this.name;
    }
}
