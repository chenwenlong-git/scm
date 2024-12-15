package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/05/30 09:02
 */
@AllArgsConstructor
@Getter
public enum DefectiveHandleType implements IRemark {

    /**
     * 已加工货
     */
    PROCESS_PRODUCT("已加工货"),

    /**
     * 加工原料
     */
    PROCESS_MATERIAL("加工原料"),

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
