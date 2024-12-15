package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/6/24 17:41
 */
@Data
@NoArgsConstructor
public class PurchaseDefaultPriceItemVo {
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "采购需求单号")
    private String purchaseParentOrderNo;
}
