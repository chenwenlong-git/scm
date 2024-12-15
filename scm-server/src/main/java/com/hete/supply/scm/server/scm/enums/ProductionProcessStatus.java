package com.hete.supply.scm.server.scm.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @Description H5加工状态
 * @Date 2023/8/29 14:13
 */
@AllArgsConstructor
@Getter
public enum ProductionProcessStatus implements IRemark {
    // 未接货
    PENDING("待加工"),
    // 已完成加工
    COMPLETED("已完成"),
    // 已接货且未完成加工
    IN_PROGRESS("加工中");
    private final String remark;
}
