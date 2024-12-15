package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum PurchaseSort implements IRemark {
    // 采购列表排序
    CREATE_TIME(1, "创建时间升序"),
    CREATE_TIME_DESC(2, "创建时间降序"),
    DELIVER_DATE(3, "要求发货时间升序"),
    DELIVER_DATE_DESC(4, "要求发货时间降序"),
    EXPECTED_ON_SHELVES_DATE(5, "期望上架时间升序"),
    EXPECTED_ON_SHELVES_DATE_DESC(6, "期望上架时间降序"),
    PLACE_ORDER_TIME(7, "下单时间升序"),
    PLACE_ORDER_TIME_DESC(8, "下单时间降序"),
    PURCHASE_CHILD_NO(9, "采购订单号升序"),
    PURCHASE_CHILD_NO_DESC(10, "采购订单号降序"),
    ;

    private final Integer sortNum;
    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

}
