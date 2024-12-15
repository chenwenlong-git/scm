package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/2/28 14:34
 */
@Data
public class GoodsOfCostExportVo {

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "库存数量")
    private Integer inventory;

    @ApiModelProperty(value = "库存金额")
    private BigDecimal inventoryPrice;

    @ApiModelProperty(value = "加权单价")
    private BigDecimal weightingPrice;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTimeStr;

    @ApiModelProperty(value = "成本时间")
    private String costTime;

    @ApiModelProperty(value = "最新接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "最新接单时间")
    private String receiveOrderTimeStr;

    @ApiModelProperty(value = "最新采购单价")
    private BigDecimal purchaseChildPurchasePrice;

    @ApiModelProperty(value = "最新核价单价")
    private BigDecimal developPricingPurchasePrice;

    @ApiModelProperty(value = "最新核价时间")
    private LocalDateTime nuclearPriceTime;

    @ApiModelProperty(value = "最新核价时间")
    private String nuclearPriceTimeStr;

}
