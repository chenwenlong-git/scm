package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:34
 */
@Data
@NoArgsConstructor
public class SupplierDetailDto {

    @NotNull(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

}
