package com.hete.supply.scm.server.scm.cost.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/2/22 18:31
 */
@Data
@NoArgsConstructor
public class CostOfGoodsIdDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long costOfGoodsId;
}
