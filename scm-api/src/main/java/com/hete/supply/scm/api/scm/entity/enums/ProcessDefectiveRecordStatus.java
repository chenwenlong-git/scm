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
public enum ProcessDefectiveRecordStatus implements IRemark {

    /**
     * 待处理
     */
    WAIT_HANDLE("待处理"),

    /**
     * 已确认
     */
    CONFIRMED("已确认"),

    /**
     * 已处理
     */
    HANDLED("已处理"),

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
