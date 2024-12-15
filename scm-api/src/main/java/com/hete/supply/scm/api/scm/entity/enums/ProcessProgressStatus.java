package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工序扫码进度状态
 *
 * @author yanjiawei
 * @date 2023/07/23 18:04
 */
@Getter
@AllArgsConstructor
public enum ProcessProgressStatus implements IRemark {
    // 工序进度状态
    UN_START("未开始"),
    PROCESSING("加工中"),
    COMPLETED("已完成"),
    ;
    private final String remark;
}