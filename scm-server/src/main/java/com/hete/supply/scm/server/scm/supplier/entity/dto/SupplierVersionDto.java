package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
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
public class SupplierVersionDto {

    @NotNull(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "供应商状态不能为空")
    @ApiModelProperty(value = "供应商状态")
    private SupplierStatus supplierStatus;

}
