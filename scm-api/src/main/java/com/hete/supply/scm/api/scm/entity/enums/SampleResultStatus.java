package com.hete.supply.scm.api.scm.entity.enums;

/**
 * @author ChenWenLong
 * @date 2023/4/14 15:17
 */

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SampleResultStatus implements IRemark {
    // 样品子单关联单据处理状态
    WAIT_HANDLE("待处理"),
    HANDLED("已处理");
    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
