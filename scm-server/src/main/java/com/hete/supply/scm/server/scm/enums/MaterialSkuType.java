package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @Description 原料SKU所属类型
 * @Date 2023/9/22 14:15
 */
@AllArgsConstructor
@Getter
public enum MaterialSkuType implements IRemark {
    // 工序模板创建选择的SKU来源于商品
    COMMODITY_SKU("商品SKU"),

    // 工序模板创建选择的SKU来源于辅料
    ACCESSORY_SKU("辅料SKU");

    private final String remark;
}
