package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2023/12/15.
 */
@Getter
@AllArgsConstructor
public enum CommissionCategory implements IRemark {
    // 提成类目
    STAIR("阶梯提成"),
    // 其他提成类目...
    ;
    private final String remark;
}

