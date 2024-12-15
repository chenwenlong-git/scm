package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/3/25 14:56
 */
@AllArgsConstructor
@Getter
public enum DevelopSampleSaleHandle implements IRemark {

    // 样品单闪售处理操作
    DISBURSEMENT_NEW("全新开款"),
    BINDING_SKU("绑定已有SKU"),
    ALREADY_BINDING("样品单存在SKU");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
