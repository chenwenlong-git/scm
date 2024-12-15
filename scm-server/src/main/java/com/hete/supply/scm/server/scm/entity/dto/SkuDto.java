package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/11/1 14:52
 */
@Data
@NoArgsConstructor
public class SkuDto {
    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;
}
