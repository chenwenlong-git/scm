package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/3 00:54
 */
@Data
@NoArgsConstructor
public class SupplierSimpleDto {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;
}
