package com.hete.supply.scm.server.scm.purchase.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/8/7 14:22
 */
@Data
@NoArgsConstructor
public class PurchaseGetSuggestSupplierBo {

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "计划确认时间")
    private LocalDateTime planConfirmTime;

}
