package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @Description 答交时间是否延期
 * @Date 2024/4/11 11:05
 */
@Getter
@AllArgsConstructor
public enum PromiseDateDelayed implements IRemark {
    // 答交时间是否延期

    // 答交时间-1天小于当前时间
    DELAYED("延期"),

    // 非答交时间-1天小于当前时间
    NOT_DELAYED("正常");

    private final String desc;


    @Override
    public String getRemark() {
        return this.desc;
    }
}
