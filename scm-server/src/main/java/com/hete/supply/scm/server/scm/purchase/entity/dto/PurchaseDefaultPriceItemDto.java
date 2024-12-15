package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/6/24 17:13
 */
@Data
@NoArgsConstructor
public class PurchaseDefaultPriceItemDto {
    @NotBlank(message = "采购订单号不能为空")
    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @NotBlank(message = "采购需求单号")
    @ApiModelProperty(value = "采购需求单号")
    private String purchaseParentOrderNo;

    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;
}
