package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2022/11/25 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderIsForward implements IRemark {

    /**
     * 是
     */
    TRUE("是"),

    /**
     * 否
     */
    FALSE("否");

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
