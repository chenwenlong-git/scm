package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2024/6/14 10:24
 */
@AllArgsConstructor
@Getter
public enum RecoOrderItemRelationType implements IRemark {

    // 关联单据类型
    DEDUCT("扣款单"),
    SUPPLEMENT("补款单"),
    ;

    private final String remark;
}
