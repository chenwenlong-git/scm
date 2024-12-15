package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/28 16:42
 */
@Data
@NoArgsConstructor
public class PurchaseRawReceiptOrderNoDto {
    @ApiModelProperty(value = "采购原料收货单号")
    private String purchaseRawReceiptOrderNo;
}
