package com.hete.supply.scm.server.scm.cost.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/2/20 14:46
 */
@Data
@NoArgsConstructor
public class SkuWarehouseDto {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
}
