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
public enum SupplierPaymentAccountType implements IRemark {
    //供应商收款账号类型
    PERSONAL_ACCOUNT("个人账号"),
    COMPANY_ACCOUNT("对公账号"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


}
