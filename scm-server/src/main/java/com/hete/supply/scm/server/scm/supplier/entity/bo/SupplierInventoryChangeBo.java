package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/8/2 15:56
 */
@Data
@NoArgsConstructor
public class SupplierInventoryChangeBo {
    @ApiModelProperty(value = "可用-备货库存")
    private Integer stockUpInventory;

    @ApiModelProperty(value = "可用-自备库存")
    private Integer selfProvideInventory;

    @ApiModelProperty(value = "可用-不良库存")
    private Integer defectiveInventory;

    @ApiModelProperty(value = "冻结-备货库存")
    private Integer frzStockUpInventory;

    @ApiModelProperty(value = "冻结-自备库存")
    private Integer frzSelfProvideInventory;

    @ApiModelProperty(value = "冻结-不良库存")
    private Integer frzDefectiveInventory;

    @NotBlank(message = "供应商code不能为空")
    @ApiModelProperty(value = "供应商code")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;
}
