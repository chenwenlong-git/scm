package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author weiwenxin
 * @date 2024/8/1 10:02
 */
@Data
@NoArgsConstructor
public class RawLocationItemDto {
    @NotNull(message = "库位出库数量不能为空")
    @Positive(message = "库位出库数量必须为正整数")
    @ApiModelProperty(value = "库位出库数量")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "库位")
    private String warehouseLocationCode;

    @NotBlank(message = "批次码不能为空")
    @ApiModelProperty(value = "批次码")
    private String batchCode;
}
