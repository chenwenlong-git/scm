package com.hete.supply.scm.server.supplier.supplier.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/9 11:46
 */
@Data
@NoArgsConstructor
public class InventoryChangeItemDto {
    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "SKU")
    private String sku;

    @NotNull(message = "操作库存类型不能为空")
    @ApiModelProperty(value = "操作库存类型")
    private SupplierWarehouse supplierWarehouse;

    @NotNull(message = "库存变更数不能为空")
    @ApiModelProperty(value = "库存变更数")
    private Integer inventoryChangeCnt;

    @Length(max = 255, message = "库存变更备注长度不能超过255个字符")
    @ApiModelProperty(value = "库存变更备注")
    private String recordRemark;
}
