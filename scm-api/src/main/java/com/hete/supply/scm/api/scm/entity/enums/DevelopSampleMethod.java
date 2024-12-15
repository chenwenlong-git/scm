package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:37
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleMethod implements IRemark {
    //处理方式
    SEAL_SAMPLE("入库", DevelopSampleDestination.SEAL_SAMPLE),
    SALE("闪售", DevelopSampleDestination.SALE),
    SAMPLE_RETURN("退样", DevelopSampleDestination.SAMPLE_RETURN);

    private final String remark;
    private final DevelopSampleDestination developSampleDestination;


    @Override
    public String getRemark() {
        return this.remark;
    }

}
