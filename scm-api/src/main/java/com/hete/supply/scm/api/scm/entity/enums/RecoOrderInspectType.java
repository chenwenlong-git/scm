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
public enum RecoOrderInspectType implements IRemark {

    // 检验类型
    PRICE("单价"),
    QUANTITY("数量"),
    ;

    private final String remark;
}
