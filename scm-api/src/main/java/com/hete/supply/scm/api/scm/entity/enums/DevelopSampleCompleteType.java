package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/11/23 15:15
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleCompleteType implements IRemark {
    //齐备信息列表信息类型
    SEAL_SAMPLE("入库"),
    SALE("闪售"),
    SAMPLE_RETURN("退样"),
    NOT_SAMPLE("无需打样"),
    NOT_SEAL_SAMPLE("非入库");

    private final String remark;

    @Override
    public String getRemark() {
        return this.remark;
    }

}
