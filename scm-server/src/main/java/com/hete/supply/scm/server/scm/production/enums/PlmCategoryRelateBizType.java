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
public enum PlmCategoryRelateBizType implements IRemark {
    //供应链属性
    ATTRIBUTE("供应链属性"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
