package com.hete.supply.scm.server.scm.entity.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2023/12/18.
 */
@Data
public class CommissionAmountBo {
    private int totalQuantity = 0;
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
