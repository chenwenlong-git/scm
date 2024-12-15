package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/9 16:31
 */
@Data
@NoArgsConstructor
public class SupplierSkuInventoryVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "备货库存")
    private Integer stockUpInventory;

    @ApiModelProperty(value = "自备库存")
    private Integer selfProvideInventory;

    @ApiModelProperty(value = "其他供应商自备库存总和")
    private Integer otherSupplierInventory;
}
