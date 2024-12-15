package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * H5工作台工序状态
 *
 * @author yanjiawei
 * @date 2023/07/23 23:49
 */
@Getter
@AllArgsConstructor
public enum ProcessingStatus implements IRemark {
    // 未接货且非当前处理工序
    WAITING("等待中"),
    // 未接货且是当前需要处理工序
    AWAITING_RECEIPT("待接货"),
    // 已扫码接货
    AWAITING_START("待开始"),
    // 已开始加工
    PROCESSING("加工中"),
    // 已完成加工
    COMPLETED("已完成");
    private final String remark;
}
