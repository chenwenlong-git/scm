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
public enum SupplementType implements IRemark {
    //补款单的类型
    PRICE("价差补款"),
    PROCESS("加工补款"),
    OTHER("其他"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
