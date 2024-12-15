package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplementDeduct implements IRemark {
    //补扣单类型
    SUPPLEMENT("补款单"),
    DEDUCT("扣款单");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
