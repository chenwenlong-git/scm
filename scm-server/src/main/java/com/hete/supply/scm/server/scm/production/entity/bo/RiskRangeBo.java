package com.hete.supply.scm.server.scm.production.entity.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/10/11.
 */
@Data
public class RiskRangeBo {
    private BigDecimal min;
    private BigDecimal max;
    private boolean isInclude;
}
