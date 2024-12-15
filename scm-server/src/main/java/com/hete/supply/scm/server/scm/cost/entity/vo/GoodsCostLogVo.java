package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/20 15:25
 */
@Data
@NoArgsConstructor
public class GoodsCostLogVo {
    @ApiModelProperty(value = "每日记录")
    private List<GoodsCostLogItemVo> yestGoodsCostLogItemList;

    @ApiModelProperty(value = "每月记录")
    private List<GoodsCostLogItemVo> moGoodsCostLogItemList;
}
