package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/1/10 10:04
 */
@Data
@NoArgsConstructor
public class SupplierWarehouseBindDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @NotBlank(message = "仓库名称不能为空")
    @ApiModelProperty(value = "仓库名称")
    @Length(max = 64, message = "仓库名称最长不能超过64个字符")
    private String warehouseName;

}
