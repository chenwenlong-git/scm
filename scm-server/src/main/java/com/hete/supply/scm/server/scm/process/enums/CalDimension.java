package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/3/13.
 */
@Getter
@AllArgsConstructor
public enum CalDimension implements IRemark {
    // 报表统计维度
    MONTHLY("月度加工人效报表统计维度"),
    DAILY("日度加工人效报表统计维度");
    private final String remark;
}

