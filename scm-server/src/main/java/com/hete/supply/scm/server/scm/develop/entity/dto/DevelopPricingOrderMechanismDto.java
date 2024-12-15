package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderMechanismDto {

    @ApiModelProperty(value = "机制毛尺寸")
    private String hairSize;

    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "机制毛金额")
    private BigDecimal hairPrice;

}
