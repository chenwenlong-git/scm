package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 质检明细导出策略枚举。
 * <p>
 * 该枚举定义了不同的导出策略，可以按照质检单或不良原因筛选导出质检明细数据。
 *
 * @author yanjiawei
 */
@Getter
@AllArgsConstructor
public enum QcExportType implements IRemark {

    // 按质检单筛选导出
    BY_QC_ORDER("按质检单"),

    // 按不良原因筛选导出
    BY_QC_ORDER_DETAIL("按质检单明细");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }
}

