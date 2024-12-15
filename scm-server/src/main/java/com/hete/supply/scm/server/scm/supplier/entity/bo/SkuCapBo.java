package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SKU单件产能
 *
 * @author yanjiawei
 * Created on 2024/8/12.
 */
@Data
@NoArgsConstructor
public class SkuCapBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产能耗时天数")
    private BigDecimal capacityDays = BigDecimal.ZERO;
}
