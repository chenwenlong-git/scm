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
public enum SupplierExport implements IRemark {
    //供应商进出口资质
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
    public static SupplierExport getSupplierExport(String supplierExport) {
        if (SupplierExport.NO.getRemark().equals(supplierExport)) {
            return NO;
        }
        if (SupplierExport.YES.getRemark().equals(supplierExport)) {
            return YES;
        }
        throw new ParamIllegalException("供应商进出口资质类型错误，存在非法类型");
    }

}
