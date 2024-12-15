package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/2/14 14:48
 */
@Data
@NoArgsConstructor
public class ShippingMarkIdAndVersionDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long shippingMarkId;

    @NotNull(message = "版本号不能为空")
    @ApiModelProperty(value = "版本号")
    private Integer version;
}
