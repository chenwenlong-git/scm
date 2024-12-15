package com.hete.supply.scm.server.scm.adjust.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:00
 */
@Data
@NoArgsConstructor
public class GoodsPriceGetSkuPurchaseListVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "渠道价格")
    private BigDecimal channelPrice;


}
