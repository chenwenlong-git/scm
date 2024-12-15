package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanjiawei
 * Created on 2024/2/21.
 */
@Data
public class SkuWeightedPriceResultBo {
    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;

    @ApiModelProperty(value = "月初加权价", example = "10.50")
    private BigDecimal monthStartWeightedPrice = BigDecimal.ZERO;
}
