package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2023/2/24 10:37
 */
@Data
@NoArgsConstructor
public class DeductOrderOtherDto {

    @NotNull(message = "扣款金额不能为空")
    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @NotBlank(message = "扣款备注不能为空")
    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;

}

