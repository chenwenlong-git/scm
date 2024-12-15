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
public enum ProcessMaterialReceiptStatus implements IRemark {

    /**
     * 待收货
     */
    WAIT_RECEIVE("待收货"),

    /**
     * 已收货
     */
    RECEIVED("已收货"),

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
