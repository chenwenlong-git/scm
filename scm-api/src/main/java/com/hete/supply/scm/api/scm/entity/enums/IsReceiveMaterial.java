package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * @date 2023/07/23 12:44
 */
@Getter
@AllArgsConstructor
public enum IsReceiveMaterial implements IRemark {
    // 是否回料
    TRUE("已回料"),
    FALSE("未回料"),
    NO_RETURN_REQUIRED("无需回料"),

    ;
    private final String remark;
}