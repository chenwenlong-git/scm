package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/1/31 14:04
 */
@Data
@NoArgsConstructor
public class SkuAvgPriceBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "累积数")
    private Integer accrueCnt;

    @ApiModelProperty(value = "累积总价")
    private BigDecimal accruePrice;

    @ApiModelProperty(value = "sku计算后的均价")
    private BigDecimal avgPrice;
}
