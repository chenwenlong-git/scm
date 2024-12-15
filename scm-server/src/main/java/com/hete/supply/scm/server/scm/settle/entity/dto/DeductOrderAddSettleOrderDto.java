package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/1/31 11:43
 */
@Data
@NoArgsConstructor
public class DeductOrderAddSettleOrderDto {

    @NotBlank(message = "扣款单号不能为空")
    @ApiModelProperty(value = "扣款单号")
    private String deductOrderNo;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;


}
