package com.hete.supply.scm.server.scm.stockup.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/1/9 20:18
 */
@Data
@NoArgsConstructor
public class StockIdAndVersionDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long stockUpOrderId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
