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
public enum DeliverOrderItemType implements IRemark {
    // 发货详情关联单据类型
    BULK("收货单"),
    QUALITY_INSPECTION("质检单"),
    SHELVES_ORDER("上架单"),
    RETURN_ORDER("退货单"),
    SETTLE_ORDER("结算单");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

}
