package com.hete.supply.scm.server.scm.develop.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/18 16:47
 */
@Getter
@AllArgsConstructor
public enum DevelopChildExceptionalType implements IRemark {
    //异常处理的处理方式
    CANCEL_DEVELOPMENT("取消开发"),
    CHANGE_SUPPLIER("更换供应商"),
    REPRINT("重新打版");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
