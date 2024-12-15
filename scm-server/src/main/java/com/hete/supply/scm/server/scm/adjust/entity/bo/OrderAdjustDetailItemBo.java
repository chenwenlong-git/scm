package com.hete.supply.scm.server.scm.adjust.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/6/18 16:17
 */
@Data
@NoArgsConstructor
public class OrderAdjustDetailItemBo {
    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "sku信息")
    private String skuMsg;

    @ApiModelProperty(value = "采购订单信息")
    private String purchaseMsg;

    @ApiModelProperty(value = "采购订单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "订单调价")
    private String orderAdjustStr;

    @ApiModelProperty(value = "订单调价")
    private BigDecimal orderAdjust;

    @ApiModelProperty(value = "调价事由")
    private String adjustReason;

    @ApiModelProperty(value = "备注")
    private String remark;
}
