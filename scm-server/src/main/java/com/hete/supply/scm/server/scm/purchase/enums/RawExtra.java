package com.hete.supply.scm.server.scm.purchase.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/3/20 10:50
 */
@Getter
@AllArgsConstructor
public enum RawExtra implements IRemark {
    // 额外原料出库:NORMAL(常规),NO_NEED_EXTRA(无需额外原料出库),
    NORMAL("常规"),
    NO_NEED_EXTRA("无需额外原料出库"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
