package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/1/16 09:50
 */
@Data
@NoArgsConstructor
public class SupplierAndSkuItemDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;
}
