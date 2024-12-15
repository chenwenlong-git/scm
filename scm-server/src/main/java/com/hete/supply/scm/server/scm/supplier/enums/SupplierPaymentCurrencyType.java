package com.hete.supply.scm.server.scm.supplier.enums;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2023/12/5 18:17
 */
@Getter
@AllArgsConstructor
public enum SupplierPaymentCurrencyType implements IRemark {
    //供应商收款货币类型
    RMB("人民币"),
    USD("美元"),
    ;

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }


    public Currency toCurrency() {
        if (this == RMB) {
            return Currency.RMB;
        }
        if (this == USD) {
            return Currency.USD;
        }
        throw new BizException("供应商账户汇率异常，请联系系统管理员!");
    }


}
