package com.hete.supply.scm.server.supplier.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/19 16:09
 */
@Data
@NoArgsConstructor
public class InventorySubItemDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;

    @NotNull(message = "备货库存变更数不能为空")
    @ApiModelProperty(value = "备货库存变更数")
    private Integer stockUpChangeInventory;

    @NotNull(message = "自备库存变更数不能为空")
    @ApiModelProperty(value = "自备库存变更数")
    private Integer selfProvideChangeInventory;
}
