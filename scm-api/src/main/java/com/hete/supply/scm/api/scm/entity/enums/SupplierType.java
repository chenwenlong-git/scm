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
public enum SupplierType implements IRemark {
    //供应商类型
    ONESELF_BUSINESS("自营供应商"),
    COOPERATION("合作供应商"),
    TEMPORARY("临时供应商");

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
    public static SupplierType getSupplierType(String supplierType) {
        if (SupplierType.ONESELF_BUSINESS.getRemark().equals(supplierType)) {
            return ONESELF_BUSINESS;
        }
        if (SupplierType.COOPERATION.getRemark().equals(supplierType)) {
            return COOPERATION;
        }
        if (SupplierType.TEMPORARY.getRemark().equals(supplierType)) {
            return TEMPORARY;
        }
        throw new ParamIllegalException("供应商类型错误，存在非法类型");
    }

}
