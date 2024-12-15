package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderScanFilterType implements IRemark {
    /**
     * 本月
     */
    MONTH("本月"),

    /**
     * 全部
     */
    ALL("全部");

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
