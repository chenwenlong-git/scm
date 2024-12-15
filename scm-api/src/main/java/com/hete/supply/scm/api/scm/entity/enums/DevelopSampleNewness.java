package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiwenxin
 * @date 2023/8/4 17:34
 */
@Getter
@AllArgsConstructor
public enum DevelopSampleNewness implements IRemark {
    // 样品需求:
    NEW_PRODUCT("新品"),
    OLD_PRODUCT("旧品"),
    ;

    private final String remark;


}
