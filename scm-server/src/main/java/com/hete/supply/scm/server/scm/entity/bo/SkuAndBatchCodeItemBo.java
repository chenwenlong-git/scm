package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/1/31 14:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuAndBatchCodeItemBo {
    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "累积数不能为空")
    @ApiModelProperty(value = "累积数")
    private Integer accrueCnt;

    @NotNull(message = "累积总价不能为空")
    @ApiModelProperty(value = "累积总价")
    private BigDecimal accruePrice;
}
