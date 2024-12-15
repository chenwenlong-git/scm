package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/2/14 09:27
 */
@Data
@NoArgsConstructor
public class ShippingMarkNoDto {
    @NotBlank(message = "箱唛号不能为空")
    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;
}
