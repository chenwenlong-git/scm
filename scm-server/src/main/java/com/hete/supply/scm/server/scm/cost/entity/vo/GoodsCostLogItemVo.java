package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/2/20 15:28
 */
@Data
@NoArgsConstructor
public class GoodsCostLogItemVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "库存金额")
    private BigDecimal inventoryPrice;

    @ApiModelProperty(value = "加权单价")
    private BigDecimal weightingPrice;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;
}
