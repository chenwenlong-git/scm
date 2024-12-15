package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 供应商产能规则类型
 *
 * @author yanjiawei
 * Created on 2024/8/5.
 */
@Getter
@AllArgsConstructor
public enum CapacityType implements IRemark {

    //常规
    NORMAL("常规");

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
