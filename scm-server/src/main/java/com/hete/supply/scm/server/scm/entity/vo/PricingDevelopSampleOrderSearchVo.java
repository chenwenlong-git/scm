package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopOrderPriceVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/15 15:17
 */
@Data
@NoArgsConstructor
public class PricingDevelopSampleOrderSearchVo {
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;

}
