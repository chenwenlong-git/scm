package com.hete.supply.scm.server.scm.cost.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/2/20 14:50
 */
@Data
@NoArgsConstructor
public class GoodsCostDetailVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;


    @ApiModelProperty(value = "最新核价单价")
    private BigDecimal developPricingPurchasePrice;

    @ApiModelProperty(value = "最新核价时间")
    private LocalDateTime nuclearPriceTime;

    @ApiModelProperty(value = "最新采购单价")
    private BigDecimal purchaseChildPurchasePrice;

    @ApiModelProperty(value = "最新接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "商品采购价格")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "商品采购价格更新时间")
    private LocalDateTime goodsPurchasePriceTime;


    @ApiModelProperty(value = "月初库存数量")
    private Integer moInventory;

    @ApiModelProperty(value = "月初库存金额")
    private BigDecimal moInventoryPrice;

    @ApiModelProperty(value = "月初加权单价")
    private BigDecimal moWeightingPrice;

    @ApiModelProperty(value = "月初更新时间")
    private LocalDateTime moUpdateTime;

    @ApiModelProperty(value = "昨日库存数量")
    private Integer yestInventory;

    @ApiModelProperty(value = "昨日库存金额")
    private BigDecimal yestInventoryPrice;

    @ApiModelProperty(value = "昨日加权单价")
    private BigDecimal yestWeightingPrice;

    @ApiModelProperty(value = "昨日更新时间")
    private LocalDateTime yestUpdateTime;

    @ApiModelProperty(value = "产品库存批次（昨日）列表")
    private List<GoodsCostItemVo> goodsCostItemList;
}
