package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/6/19 11:49
 */
@Data
@NoArgsConstructor
public class GoodsPriceExportVo {

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "渠道")
    private String channelName;

    @ApiModelProperty(value = "渠道价格")
    private BigDecimal channelPrice;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "生效日期-渠道价格")
    private String effectiveTimeStr;

}
