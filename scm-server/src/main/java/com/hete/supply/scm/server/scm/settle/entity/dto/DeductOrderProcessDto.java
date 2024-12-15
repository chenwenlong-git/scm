package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2022/12/30 11:53
 */
@Data
@NoArgsConstructor
public class DeductOrderProcessDto {

    @ApiModelProperty(value = "加工单号ID")
    private Long processOrderId;

    @NotBlank(message = "加工单号不能为空")
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @NotNull(message = "扣款金额不能为空")
    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductPrice;

    @NotBlank(message = "扣款备注不能为空")
    @ApiModelProperty(value = "扣款备注")
    private String deductRemarks;
}