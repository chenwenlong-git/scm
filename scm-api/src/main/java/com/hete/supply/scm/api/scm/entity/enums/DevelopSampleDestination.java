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
public enum DevelopSampleDestination implements IRemark {
    //样品去向
    SEAL_SAMPLE("入库"),
    SALE("闪售"),
    SAMPLE_RETURN("退样"),
    NOT_SAMPLE("无需打样");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
