package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/3/21 15:37
 */
@AllArgsConstructor
@Getter
public enum DevelopSampleDirection implements IRemark {

    // 开发样品单货物走向
    WAREHOUSING("入库"),
    RETURN_SAMPLES("退样");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
