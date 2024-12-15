package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/9/10 10:48
 */
@Data
@NoArgsConstructor
public class SkuAttrPriceIdAndVersionDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long skuAttrPriceId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
