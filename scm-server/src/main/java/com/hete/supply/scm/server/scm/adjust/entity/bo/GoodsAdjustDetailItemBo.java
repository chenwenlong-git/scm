package com.hete.supply.scm.server.scm.adjust.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/6/18 16:19
 */
@Data
@NoArgsConstructor
public class GoodsAdjustDetailItemBo {
    @ApiModelProperty(value = "商品调价明细的ID")
    private Long goodsPriceItemId;


    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "渠道价格（前）")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "渠道价格（后）")
    private BigDecimal adjustPrice;

    @ApiModelProperty(value = "渠道价格（前）")
    private String originalPriceStr;

    @ApiModelProperty(value = "渠道价格（后）")
    private String adjustPriceStr;

    @ApiModelProperty(value = "生效时间")
    private String effectiveTimeStr;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "生效备注")
    private String effectiveRemark;

    @ApiModelProperty(value = "设置通用")
    private String universal;
}
