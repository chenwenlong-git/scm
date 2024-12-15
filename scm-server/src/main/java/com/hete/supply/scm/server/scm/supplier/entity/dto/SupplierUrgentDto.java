package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/07/18 14:34
 */
@Data
@NoArgsConstructor
public class SupplierUrgentDto {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotNull(message = "时间差值不能为空")
    @ApiModelProperty(value = "时间差值(天数)")
    private BigDecimal timeValue;

}
