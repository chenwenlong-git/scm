package com.hete.supply.scm.server.scm.supplier.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/1/23 18:05
 */
@Data
@NoArgsConstructor
public class SupplierWareSearchItemVo {
    @ApiModelProperty(value = "id")
    private Long supplierWarehouseId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库类型")
    private SupplierWarehouse supplierWarehouse;
}
