package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/2/29 10:37
 */
@Data
@NoArgsConstructor
public class CostSkuItemVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "库存价格")
    private BigDecimal inventoryPrice;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
