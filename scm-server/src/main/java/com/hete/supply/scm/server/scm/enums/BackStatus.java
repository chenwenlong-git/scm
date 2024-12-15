package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: RockyHuas
 * @date: 2023/03/17 15:02
 */
@AllArgsConstructor
@Getter
public enum BackStatus implements IRemark {
    /**
     * 待归还
     */
    WAIT_BACK("待归还"),
    /**
     * 已归还
     */
    COMPLETED_BACK("已归还");

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
