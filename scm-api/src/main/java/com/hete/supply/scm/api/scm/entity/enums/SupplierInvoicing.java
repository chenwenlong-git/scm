package com.hete.supply.scm.api.scm.entity.enums;

import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplierInvoicing implements IRemark {
    //供应商开票资质
    NO("否"),
    YES("是");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    /**
     * 获取对应的类型
     *
     * @author ChenWenLong
     * @date 2022/11/28 15:59
     */
    public static SupplierInvoicing getSupplierInvoicing(String supplierInvoicing) {
        if (SupplierInvoicing.NO.getRemark().equals(supplierInvoicing)) {
            return NO;
        }
        if (SupplierInvoicing.YES.getRemark().equals(supplierInvoicing)) {
            return YES;
        }
        throw new ParamIllegalException("供应商开票资质类型错误，存在非法类型");
    }

}
