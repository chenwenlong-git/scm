package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/03/17 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderOriginal implements IRemark {
    /**
     * 页面正常创建
     */
    NORMAL("正常"),

    /**
     * 来自 WMS 创建
     */
    WMS("WMS"),

    /**
     * 返工创建
     */
    REWORKING("返工"),

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
