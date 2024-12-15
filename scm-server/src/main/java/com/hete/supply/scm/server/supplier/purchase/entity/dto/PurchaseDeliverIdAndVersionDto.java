package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/3 12:42
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverIdAndVersionDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long purchaseDeliverOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
