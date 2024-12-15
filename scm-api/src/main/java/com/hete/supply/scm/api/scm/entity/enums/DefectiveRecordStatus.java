package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/05/16 09:02
 */
@AllArgsConstructor
@Getter
public enum DefectiveRecordStatus implements IRemark {

    /**
     * 次品待处理
     */
    DEFECTIVE_WAIT_HANDLE("次品待处理"),

    /**
     * 次品已处理
     */
    DEFECTIVE_HANDLED("次品已处理"),

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
