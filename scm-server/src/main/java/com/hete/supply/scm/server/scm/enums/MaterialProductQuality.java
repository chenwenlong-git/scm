package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rockyHuas
 * @date 2023/05/06 11:21
 */
@Getter
@AllArgsConstructor
public enum MaterialProductQuality implements IRemark {
    // 产品质量
    DEFECTIVE("不良品"),
    GOOD("良品"),
    ;


    private final String remark;

}
