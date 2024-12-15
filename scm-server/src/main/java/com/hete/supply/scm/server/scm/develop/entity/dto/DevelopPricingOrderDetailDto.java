package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:17
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderDetailDto {
    @NotBlank(message = "核价单号不能为空")
    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;
}
