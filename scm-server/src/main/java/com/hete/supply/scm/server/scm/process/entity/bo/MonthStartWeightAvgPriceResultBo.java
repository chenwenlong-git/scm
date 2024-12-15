package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/3/20.
 */
@Data
public class MonthStartWeightAvgPriceResultBo {
    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;

    @ApiModelProperty(value = "月初加权平均价", example = "10.50")
    private BigDecimal monthStartWeightedAveragePrice = BigDecimal.ZERO;
}
