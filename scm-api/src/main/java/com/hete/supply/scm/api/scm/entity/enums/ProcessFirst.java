package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 15:02
 */
@AllArgsConstructor
@Getter
public enum ProcessFirst implements IRemark {

    /**
     * 前处理
     */
    WAIT_HANDLE("前处理", "01"),

    /**
     * 缝制中
     */
    HANDLING("缝制中", "02"),

    /**
     * 后处理
     */
    HANDLED("后处理", "03"),

    ;


    /**
     * 描述
     */
    private final String desc;

    private final String code;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
