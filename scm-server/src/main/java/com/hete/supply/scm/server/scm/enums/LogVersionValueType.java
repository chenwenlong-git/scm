package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/12/2 20:16
 */
@AllArgsConstructor
@Getter
public enum LogVersionValueType implements IRemark {
    // 日志版本值类型
    STRING("字符串"),
    LIST("列表"),
    DATE("时间类型"),
    ;

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }

}
