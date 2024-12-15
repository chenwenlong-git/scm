package com.hete.supply.scm.api.scm.entity.enums;

/**
 * @author weiwenxin
 * @date 2022/11/2 22:11
 */

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SampleResult implements IRemark {
    // 选样结果
    WAIT_SAMPLE("待选样"),
    SAMPLE_SUCCESS("打样成功"),
    SAMPLE_RETURN("失败退样"),
    FAIL_SALE("失败闪售"),
    ;
    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }
}
