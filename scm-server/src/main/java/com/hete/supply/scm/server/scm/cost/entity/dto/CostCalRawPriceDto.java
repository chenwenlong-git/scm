package com.hete.supply.scm.server.scm.cost.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/28 11:13
 */
@Data
@NoArgsConstructor
public class CostCalRawPriceDto {
    @NotEmpty(message = "原料计算成本列表不能为空")
    @Valid
    @ApiModelProperty(value = "原料计算成本列表")
    private List<CostCalRawPriceItemDto> costCalRawPriceItemList;
}
