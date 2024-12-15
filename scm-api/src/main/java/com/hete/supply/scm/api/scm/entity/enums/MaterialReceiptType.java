package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/1/3.
 */
@Getter
@AllArgsConstructor
public enum MaterialReceiptType implements IRemark {
    // 原料收货类型
    PROCESSING_MATERIAL("加工原料"),
    REPAIR_MATERIAL("返修原料");
    private final String remark;
}
