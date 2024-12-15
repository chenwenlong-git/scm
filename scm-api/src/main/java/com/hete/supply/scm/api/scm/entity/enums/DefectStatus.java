package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum DefectStatus implements IRemark {
    //供应商状态
    ENABLED("启用"),
    DISABLED("禁用");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
