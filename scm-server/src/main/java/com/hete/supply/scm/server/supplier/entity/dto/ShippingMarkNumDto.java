package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/4/27 10:42
 */
@Data
@NoArgsConstructor
public class ShippingMarkNumDto {

    @NotBlank(message = "箱唛箱号（序号）不能为空")
    @ApiModelProperty(value = "箱唛箱号（序号）")
    private String shippingMarkNum;
}
