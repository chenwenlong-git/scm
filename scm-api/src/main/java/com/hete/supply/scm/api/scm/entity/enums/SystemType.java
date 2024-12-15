package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SystemType implements IRemark {
    //扣款单的类型
    SCM("scm"),
    SPM("spm"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
