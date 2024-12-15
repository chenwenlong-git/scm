package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/28 11:21
 */
@Getter
@AllArgsConstructor
public enum SplitType implements IRemark {
    // 拆单补交类型:SUPPLIER_SPLIT(供应商拆单),FOLLOW_SPLIT(跟单拆单),GOODS_SPLIT(商品拆单),SUGGEST_SPLIT(推荐拆单)
    SUPPLIER_SPLIT("供应商拆单"),
    FOLLOW_SPLIT("跟单拆单"),
    GOODS_SPLIT("商品拆单"),
    SUGGEST_SPLIT("推荐拆单"),
    ;


    private final String remark;
}
