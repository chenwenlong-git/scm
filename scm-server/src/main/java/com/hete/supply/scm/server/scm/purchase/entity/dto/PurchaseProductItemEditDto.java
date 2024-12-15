package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/16 09:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseProductItemEditDto extends PurchaseProductItemDto {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderItemId;

    @ApiModelProperty(value = "version")
    private Integer version;
}
