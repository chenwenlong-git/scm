package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/03/13 09:02
 */
@AllArgsConstructor
@Getter
public enum MaterialBackStatus implements IRemark {

    /**
     * 无需归还
     */
    NO_BACK("无需归还"),

    /**
     * 部分归还
     */
    PARTIAL_BACK("部分归还"),

    /**
     * 未归还
     */
    UN_BACK("未归还"),

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
