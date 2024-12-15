package com.hete.supply.scm.server.scm.develop.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/1/31 15:28
 */
@Data
@NoArgsConstructor
public class DevelopChildBatchCodeCostPriceBo {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "批次码单价不能为空")
    @ApiModelProperty(value = "批次码单价")
    private BigDecimal price;
}
