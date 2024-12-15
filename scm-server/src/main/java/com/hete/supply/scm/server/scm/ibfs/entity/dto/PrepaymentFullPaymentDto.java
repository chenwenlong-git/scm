package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/5/30 19:33
 */
@Data
public class PrepaymentFullPaymentDto {
    @NotBlank(message = "预付款单号不能为空")
    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;
}
