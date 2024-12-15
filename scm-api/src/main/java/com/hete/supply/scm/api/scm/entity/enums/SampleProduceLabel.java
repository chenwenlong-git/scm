package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/5/12 11:02
 */
@Getter
@AllArgsConstructor
public enum SampleProduceLabel implements IRemark {
    // 生产标签
    FIRST("首选"),
    EFFECTIVE("生效"),
    INVALID("失效");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


}
