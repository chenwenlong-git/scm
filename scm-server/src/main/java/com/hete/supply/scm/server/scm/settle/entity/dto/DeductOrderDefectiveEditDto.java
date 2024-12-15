package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/6/21 18:32
 */
@Data
@NoArgsConstructor
public class DeductOrderDefectiveEditDto {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long deductOrderDefectiveId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "扣款数量不能为空")
    @ApiModelProperty(value = "扣款数量")
    private Integer deductNum;

    @NotNull(message = "扣款金额不能为空")
    @ApiModelProperty(value = "需扣款单价")
    private BigDecimal deductUnitPrice;

    @NotNull(message = "扣款总价不能为空")
    @ApiModelProperty(value = "扣款总价")
    private BigDecimal deductPrice;


}

