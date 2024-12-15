package com.hete.supply.scm.server.scm.purchase.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/9/30 16:50
 */
@Data
@NoArgsConstructor
public class PurchaseLatestPriceItemBo {
    @ApiModelProperty(value = "结算金额")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

}
