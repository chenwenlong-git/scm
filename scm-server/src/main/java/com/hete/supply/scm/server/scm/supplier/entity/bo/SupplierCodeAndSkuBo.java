package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/1/12 10:03
 */
@Data
@NoArgsConstructor
public class SupplierCodeAndSkuBo {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;
}
