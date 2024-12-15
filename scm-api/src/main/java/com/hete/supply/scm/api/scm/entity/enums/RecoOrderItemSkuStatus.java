package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/5/13 15:17
 */
@AllArgsConstructor
@Getter
public enum RecoOrderItemSkuStatus implements IRemark {

    // 对账单详情单据的状态
    WAIT_CONFIRM("待确认"),
    CONFIRMED("已确认"),
    EXCEPTION("校验异常"),
    ;

    private final String desc;

    @Override
    public String getRemark() {
        return this.desc;
    }
}
