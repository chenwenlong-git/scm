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
public enum ProcessType implements IRemark {
    // 工序类型
    COMPOUND_PROCESS("复合工序"),
    INDEPENDENT_PROCESS("独立工序");

    private final String remark;
}

