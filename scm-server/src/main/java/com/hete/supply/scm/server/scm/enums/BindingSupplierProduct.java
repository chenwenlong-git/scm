package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/3/28 19:49
 */
@AllArgsConstructor
@Getter
public enum BindingSupplierProduct implements IRemark {

    //是否绑定
    TRUE("绑定"),
    FALSE("未绑定");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
