package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2022/11/18 18:03
 */
@Getter
@AllArgsConstructor
public enum ShelvesType implements IRemark {
    // 上架类型:ON_SHELVES(上架),DOWN_SHELVES(下架),
    ON_SHELVES("上架"),
    DOWN_SHELVES("下架"),
    ;

    private final String remark;

    public ShelvesType toChangeType() {
        return this.equals(ShelvesType.ON_SHELVES) ? DOWN_SHELVES : ON_SHELVES;
    }
}
