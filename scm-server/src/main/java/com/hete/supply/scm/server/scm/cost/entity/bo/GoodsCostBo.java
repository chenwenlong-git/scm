package com.hete.supply.scm.server.scm.cost.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/2/22 17:14
 */
@Data
@NoArgsConstructor
public class GoodsCostBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库聚合维度")
    private PolymerizeWarehouse polymerizeWarehouse;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "库存金额")
    private BigDecimal inventoryPrice;

    @ApiModelProperty(value = "加权单价")
    private BigDecimal weightingPrice;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
