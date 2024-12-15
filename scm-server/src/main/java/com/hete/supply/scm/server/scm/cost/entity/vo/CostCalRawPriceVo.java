package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/28 11:44
 */
@Data
@NoArgsConstructor
public class CostCalRawPriceVo {
    @ApiModelProperty(value = "原料成本列表")
    private List<CostCalRawPriceItemVo> costCalRawPriceItemList;
}
