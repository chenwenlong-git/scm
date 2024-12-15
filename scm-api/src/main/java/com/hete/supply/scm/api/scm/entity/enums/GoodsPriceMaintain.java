package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/6/20 11:37
 */
@Getter
@AllArgsConstructor
public enum GoodsPriceMaintain implements IRemark {
    // 是否维护sku商品价格状态:WAITING_MAINTAIN(待维护),MAINTAIN(已维护)
    WAITING_MAINTAIN("待维护"),
    MAINTAIN("已维护"),
    ;

    private final String remark;
}
