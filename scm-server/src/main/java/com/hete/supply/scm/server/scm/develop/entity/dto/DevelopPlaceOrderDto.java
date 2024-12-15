package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2024/8/26 18:23
 */
@Data
@NoArgsConstructor
public class DevelopPlaceOrderDto {
    @NotBlank(message = "spu不能为空")
    @ApiModelProperty(value = "spu")
    private String spu;
}
