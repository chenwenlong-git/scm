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
public enum GoodsProcessStatus implements IRemark {
    /**
     * 绑定
     */
    BINDED("绑定"),

    /**
     * 未绑定
     */
    UNBINDED("未绑定");

    /**
     * 描述
     */
    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
