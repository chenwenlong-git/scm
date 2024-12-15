package com.hete.supply.scm.server.scm.settle.enums;

import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.support.api.constant.IRemark;
import com.hete.support.api.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:46
 */
@Getter
@AllArgsConstructor
public enum SupplementDeductType implements IRemark {
    //单据类型：价差款(PRICE)、加工款(PROCESS)、品质款(QUALITY)
    PRICE("价差款"),
    PROCESS("加工款"),
    QUALITY("品质款"),
    OTHER("其他"),
    PAY("预付款"),
    DEFECTIVE_RETURN("次品退供");

    private final String name;

    @Override
    public String getRemark() {
        return this.name;
    }

    public static SupplementDeductType getSupplementDeductBySupplement(SupplementType supplementType) {
        if (supplementType.equals(SupplementType.PRICE)) {
            return SupplementDeductType.PRICE;
        }
        if (supplementType.equals(SupplementType.PROCESS)) {
            return SupplementDeductType.PROCESS;
        }
        if (supplementType.equals(SupplementType.OTHER)) {
            return SupplementDeductType.OTHER;
        }
        throw new BizException("类型错误，补款单存在非法类型");
    }

    public static SupplementDeductType getSupplementDeductByDeduct(DeductType deductType) {
        if (deductType.equals(DeductType.PRICE)) {
            return SupplementDeductType.PRICE;
        }
        if (deductType.equals(DeductType.PROCESS)) {
            return SupplementDeductType.PROCESS;
        }
        if (deductType.equals(DeductType.QUALITY)) {
            return SupplementDeductType.QUALITY;
        }
        if (deductType.equals(DeductType.OTHER)) {
            return SupplementDeductType.OTHER;
        }
        if (deductType.equals(DeductType.PAY)) {
            return SupplementDeductType.PAY;
        }
        if (deductType.equals(DeductType.DEFECTIVE_RETURN)) {
            return SupplementDeductType.DEFECTIVE_RETURN;
        }
        throw new BizException("类型错误，扣款单存在非法类型");
    }
}
