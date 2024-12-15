package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/11/9 16:59
 */
@Data
@NoArgsConstructor
public class PurchaseChildPreConfirmExportVo {
    @ApiModelProperty(value = "需求平台（平台名称）")
    private String platformName;

    @ApiModelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "期望上架时间")
    private String expectedOnShelvesDate;

    @ApiModelProperty(value = "业务约定交期")
    private String deliverDate;

    @ApiModelProperty("采购子单号")
    private String purchaseChildOrderNo;

    // 以下是不需要初始化的占位字段

    @ApiModelProperty("采购类型")
    private String b;

    @ApiModelProperty("收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty("采购单价")
    private String d;

    @ApiModelProperty("优惠金额")
    private String e;

    @ApiModelProperty("原料sku1")
    private String f1;

    @ApiModelProperty("单位bom需求数")
    private String g1;

    @ApiModelProperty("原料sku1")
    private String f2;

    @ApiModelProperty("单位bom需求数")
    private String g2;

    @ApiModelProperty("原料sku1")
    private String f3;

    @ApiModelProperty("单位bom需求数")
    private String g3;

    @ApiModelProperty("原料sku1")
    private String f4;

    @ApiModelProperty("单位bom需求数")
    private String g4;

    @ApiModelProperty("原料sku1")
    private String f5;

    @ApiModelProperty("单位bom需求数")
    private String g5;


}
