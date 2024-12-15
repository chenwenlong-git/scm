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
public enum SupplierGrade implements IRemark {
    //供应商等级 战略/重要/一般/淘汰
    STRATEGY("战略", 1),
    IMPORTANT("重要", 2),
    COMMONLY("一般", 3),
    ELIMINATE("淘汰", 4),
    ;

    private final String remark;
    private final Integer sort;

    /**
     * 获取对应的类型
     *
     * @author ChenWenLong
     * @date 2022/11/28 15:59
     */
    public static SupplierGrade getSupplierGrade(String supplierGrade) {
        if (SupplierGrade.STRATEGY.getRemark().equals(supplierGrade)) {
            return STRATEGY;
        }
        if (SupplierGrade.IMPORTANT.getRemark().equals(supplierGrade)) {
            return IMPORTANT;
        }
        if (SupplierGrade.COMMONLY.getRemark().equals(supplierGrade)) {
            return COMMONLY;
        }
        if (SupplierGrade.ELIMINATE.getRemark().equals(supplierGrade)) {
            return ELIMINATE;
        }
        throw new ParamIllegalException("供应商等级类型错误，存在非法类型");
    }
}
