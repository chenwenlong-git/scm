package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/07/01 11:55
 */
@Getter
@AllArgsConstructor
public enum DeliverOrderType implements IRemark {
    // 发货类型
    BULK("正常大货"),
    RETURN_RESEND("退货重发"),
    SUPPLEMENTARY("补交"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

}
