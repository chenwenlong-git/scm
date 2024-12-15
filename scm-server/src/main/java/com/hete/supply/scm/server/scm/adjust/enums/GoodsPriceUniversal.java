package com.hete.supply.scm.server.scm.adjust.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/6/20 11:37
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceUniversal implements IRemark {
    // 审批状态:UNIVERSAL(通用),NO_UNIVERSAL(不通用)
    UNIVERSAL("通用"),
    NO_UNIVERSAL("不通用"),
    ;

    private final String remark;
}
