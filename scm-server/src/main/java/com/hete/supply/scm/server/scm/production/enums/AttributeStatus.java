package com.hete.supply.scm.server.scm.production.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@AllArgsConstructor
@Getter
public enum AttributeStatus implements IRemark {
    //启用
    ENABLE("启用"),
    //停用
    DISABLE("停用"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
