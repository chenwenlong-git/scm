package com.hete.supply.scm.server.scm.process.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2023/9/19.
 */
@AllArgsConstructor
@Getter
public enum ProcessOrderValidationErrorMessage implements IRemark {

    // 加工单信息不存在，请刷新页面异常信息
    PROCESS_ORDER_NOT_FOUND_PLEASE_REFRESH("加工单信息不存在，请刷新页面"),
    PROCESS_ORDER_NOT_FOUND_PLEASE_CHECK_SCAN("加工单信息不存在，请校验后扫码");

    private final String messageTemplate;

    @Override
    public String getRemark() {
        return this.messageTemplate;
    }
}
