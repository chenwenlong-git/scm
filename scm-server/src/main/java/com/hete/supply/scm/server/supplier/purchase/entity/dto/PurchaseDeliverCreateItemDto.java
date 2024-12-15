package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/25 10:37
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverCreateItemDto {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "发货数不能为空")
    @ApiModelProperty(value = "发货数")
    @Min(value = 1, message = "发货数不能小于0")
    private Integer deliverCnt;

    @ApiModelProperty(value = "spu")
    private String spu;

    @NotNull(message = "采购数量不能为空")
    @ApiModelProperty(value = "采购数量")
    @Min(value = 1, message = "发货数不能小于0")
    private Integer purchaseCnt;

}
