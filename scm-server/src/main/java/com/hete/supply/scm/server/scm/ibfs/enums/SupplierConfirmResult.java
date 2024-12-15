package com.hete.supply.scm.server.scm.ibfs.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanjiawei
 * Created on 2024/6/3.
 */
@AllArgsConstructor
@Getter
public enum SupplierConfirmResult implements IRemark {

    // 跟单确认结果类型
    AGREE("同意"),
    REJECT("拒绝");
    private final String remark;
}
