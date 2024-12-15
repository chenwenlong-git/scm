package com.hete.supply.scm.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author yanjiawei
 * Created on 2023/12/17.
 */
public class ScmAmountCalculateUtil {

    /**
     * 计算金额结果保留两位小数四舍五入
     *
     * @param quantity  数量
     * @param unitPrice 单价
     * @return 金额
     */
    public static BigDecimal calculateAmountWithHalfUp(int quantity,
                                                       BigDecimal unitPrice,
                                                       int newScale) {
        if (quantity < 0 || unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("计算总异常:数量<0/单位价格为空/单位价格小于0");
        }

        return BigDecimal.valueOf(quantity)
                .multiply(unitPrice)
                .setScale(newScale, RoundingMode.HALF_UP);
    }
}
