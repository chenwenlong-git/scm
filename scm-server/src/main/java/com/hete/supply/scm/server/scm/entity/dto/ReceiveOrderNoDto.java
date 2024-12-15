package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/7/10 11:43
 */
@Data
@NoArgsConstructor
public class ReceiveOrderNoDto {

    @NotBlank(message = "收货单号不能为空")
    @ApiModelProperty(value = "收货单号")
    private String receiptOrderNo;
}
