package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/6/21 18:32
 */
@Data
@NoArgsConstructor
public class DeductOrderDefectiveDto {

    @NotBlank(message = "单据号不能为空")
    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @NotBlank(message = "批次码不能为空")
    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @NotNull(message = "扣款数量不能为空")
    @ApiModelProperty(value = "扣款数量")
    private Integer deductNum;

    @NotNull(message = "扣款金额不能为空")
    @ApiModelProperty(value = "需扣款单价")
    private BigDecimal deductUnitPrice;

    @NotNull(message = "扣款总价不能为空")
    @ApiModelProperty(value = "扣款总价")
    private BigDecimal deductPrice;

    @NotNull(message = "原结算单价不能为空")
    @ApiModelProperty(value = "原结算单价")
    private BigDecimal settlePrice;

    @NotBlank(message = "扣款原因不能为空")
    @ApiModelProperty(value = "扣款原因")
    private String deductRemarks;

}

