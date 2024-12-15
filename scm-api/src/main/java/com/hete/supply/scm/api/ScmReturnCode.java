package com.hete.supply.scm.api;

import com.hete.support.api.constant.IReturnCode;
import lombok.RequiredArgsConstructor;

/**
 * @author auto
 */
@RequiredArgsConstructor
public enum ScmReturnCode implements IReturnCode {

    //  如果没有定义message的话   抛错的时候就要手动加message
    //  如果定义了就直接override方法

    TEST_CODE("请删除该枚举"),
    ;

    /**
     * 状态码信息
     */
    private final String message;


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getName() {
        return this.name();
    }

}
