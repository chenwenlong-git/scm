package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:21
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderMechanismVo {

    @ApiModelProperty(value = "机制毛尺寸")
    private String hairSize;

    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "机制毛金额")
    private BigDecimal hairPrice;

}
