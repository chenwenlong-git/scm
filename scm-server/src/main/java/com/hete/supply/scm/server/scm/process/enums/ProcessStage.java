package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 加工环节枚举
 *
 * @author yanjiawei
 * Created on 2023/8/30.
 */
@Getter
@AllArgsConstructor
public enum ProcessStage implements IRemark {
    // 工序接货
    RECEIVING("工序接货"),
    // 工序加工
    PROCESSING("工序加工"),
    // 工序完工
    COMPLETION("工序完工");

    private final String remark;
}






