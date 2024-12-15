package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/24 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopChildOrderStatusListVo {

    @ApiModelProperty(value = "开发子单状态(加粗)")
    private List<DevelopChildOrderStatusVo> developChildOrderStatusList;

    @ApiModelProperty(value = "版单状态")
    private List<DevelopPamphletOrderStatusVo> developPamphletOrderStatusList;

    @ApiModelProperty(value = "审版单状态")
    private List<DevelopReviewOrderStatusVo> developReviewOrderStatusList;

    @ApiModelProperty(value = "核价单")
    private List<DevelopPricingOrderStatusVo> developPricingOrderStatusList;


}
