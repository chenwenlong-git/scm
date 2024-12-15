package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/10/23 17:14
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleType implements IRemark {
    //类型
    NORMAL("常规"),
    PRENATAL_SAMPLE("产前样");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
