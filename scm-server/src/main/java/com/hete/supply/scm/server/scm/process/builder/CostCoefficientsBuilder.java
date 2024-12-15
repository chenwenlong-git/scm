package com.hete.supply.scm.server.scm.process.builder;

import com.hete.supply.scm.server.scm.process.entity.po.CostCoefficientsPo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/2/20.
 */
public class CostCoefficientsBuilder {
    public static CostCoefficientsPo buildCostCoefficientsPo(LocalDateTime effectiveTime,
                                                             BigDecimal coefficient) {
        CostCoefficientsPo po = new CostCoefficientsPo();
        po.setEffectiveTime(effectiveTime);
        po.setCoefficient(coefficient);
        return po;
    }
}
