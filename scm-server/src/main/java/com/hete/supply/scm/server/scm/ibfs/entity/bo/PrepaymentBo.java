package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/5/27 11:06
 */
@Data
@NoArgsConstructor
public class PrepaymentBo {

    @ApiModelProperty(value = "预付款单ID")
    private Long prepaymentOrderId;

    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @ApiModelProperty(value = "可抵扣金额(rmb)")
    private BigDecimal canDeductionMoney;

    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;
}
