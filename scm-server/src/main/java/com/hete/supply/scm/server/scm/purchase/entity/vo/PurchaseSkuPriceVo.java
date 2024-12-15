package com.hete.supply.scm.server.scm.purchase.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/5/26 18:13
 */
@Data
@NoArgsConstructor
public class PurchaseSkuPriceVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;
}
