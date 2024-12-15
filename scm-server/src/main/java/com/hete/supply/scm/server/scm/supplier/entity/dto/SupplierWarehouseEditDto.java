package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/10 10:04
 */
@Data
@NoArgsConstructor
public class SupplierWarehouseEditDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long supplierWarehouseId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "仓库名称不能为空")
    @ApiModelProperty(value = "仓库名称")
    @Length(max = 64, message = "仓库名称最长不能超过64个字符")
    private String warehouseName;

}
