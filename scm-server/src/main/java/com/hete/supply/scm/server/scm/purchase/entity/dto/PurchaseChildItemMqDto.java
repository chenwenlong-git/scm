package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/8/20 13:47
 */
@Data
@NoArgsConstructor
public class PurchaseChildItemMqDto {
    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "sku可售数量")
    private Integer canSaleAmount;

    @ApiModelProperty(value = "sku日销数量")
    private Integer daySaleAmount;

}
