package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/30 16:18
 */
@Getter
@AllArgsConstructor
public enum RawSupplier implements IRemark {
    // 原料提供方:HETE(赫特),SUPPLIER(供应商),OTHER_SUPPLIER(其他供应商)
    HETE("赫特"),
    SUPPLIER("供应商"),
    OTHER_SUPPLIER("其他供应商"),
    ;

    private final String remark;
}
