package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/2/28 13:51
 */
@Data
@NoArgsConstructor
public class CostCalRawPriceItemVo {
    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "我司原料价格")
    private BigDecimal substractPrice;
}
