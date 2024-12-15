package com.hete.supply.scm.server.scm.stockup.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:15
 */
@Data
@NoArgsConstructor
public class StockUpOrderNoDto {
    @NotBlank(message = "备货单号不能为空")
    @ApiModelProperty(value = "备货单号")
    private String stockUpOrderNo;
}
