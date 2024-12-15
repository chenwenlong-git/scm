package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.support.api.constant.IRemark;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/12/5 18:17
 */
@Getter
@AllArgsConstructor
public enum SupplierPaymentAccountStatus implements IRemark {
    //供应商收款账号状态
    WAIT_EFFECTIVE_EXAMINE("待生效审核"),
    EFFECTIVE("生效"),
    WAIT_INVALID_EXAMINE("待失效审核"),
    INVALID("失效"),
    REFUSED("已拒绝"),
    CREATE_FAIL("创建失败"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


}
