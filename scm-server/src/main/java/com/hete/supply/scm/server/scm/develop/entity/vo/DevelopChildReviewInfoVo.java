package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/14 16:04
 */
@Data
@NoArgsConstructor
public class DevelopChildReviewInfoVo {

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "样品核价")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "核价备注")
    private String remarks;

    @ApiModelProperty(value = "核价单渠道大货报价列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;

}
