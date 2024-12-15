package com.hete.supply.scm.server.scm.develop.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/2/27 11:20
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderInfoByPriceTimeBo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "核价单号")
    private String developPricingOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "核价时间")
    private LocalDateTime nuclearPriceTime;

    @ApiModelProperty(value = "大货价格")
    private BigDecimal bulkPrice;

}
