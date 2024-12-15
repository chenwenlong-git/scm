package com.hete.supply.scm.server.scm.adjust.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2024/6/13 14:42
 */
@Getter
@AllArgsConstructor
public enum ApproveType implements IRemark {
    // 审批类型：PURCHASE_ADJUST(采购调价),GOODS_ADJUST(商品调价),
    PURCHASE_ADJUST("采购调价"),
    GOODS_ADJUST("商品调价"),
    ;

    private final String remark;
}
