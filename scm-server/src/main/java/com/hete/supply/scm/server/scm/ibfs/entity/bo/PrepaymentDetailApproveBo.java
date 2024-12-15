package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/5/15 17:25
 */
@Data
@NoArgsConstructor
public class PrepaymentDetailApproveBo {
    private String detailType;
    private BigDecimal prepaymentDetailMoney;
    private Currency currency;
    private String rmbText;
}
