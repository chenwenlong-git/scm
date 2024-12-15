package com.hete.supply.scm.server.scm.enums.wms;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author rockyHuas
 * @date 2023/03/29 11:21
 */
@Getter
@AllArgsConstructor
public enum SkuDevType implements IRemark {

    /**
     * 常规
     */
    NORMAL("常规"),

    /**
     * limited
     */
    LIMITED("limited"),
    ;

    private final String remark;
}
