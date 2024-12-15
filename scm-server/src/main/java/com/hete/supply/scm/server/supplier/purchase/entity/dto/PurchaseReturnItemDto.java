package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/20 17:16
 */
@Data
@NoArgsConstructor
public class PurchaseReturnItemDto {
    @ApiModelProperty(value = "id")
    private Long purchaseReturnOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "收货数量不能为空")
    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

}
