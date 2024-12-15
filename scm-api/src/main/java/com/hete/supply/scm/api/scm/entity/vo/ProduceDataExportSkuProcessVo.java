package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ChenWenLong
 * @date 2024/1/2 11:55
 */
@Data
public class ProduceDataExportSkuProcessVo {

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "原料SKU")
    private String rawSku;

    @ApiModelProperty(value = "原料配比")
    private Integer skuCnt;

    @ApiModelProperty(value = "工序1")
    private String process1;

    @ApiModelProperty(value = "工序2")
    private String process2;

    @ApiModelProperty(value = "工序3")
    private String process3;

    @ApiModelProperty(value = "工序4")
    private String process4;

    @ApiModelProperty(value = "工序5")
    private String process5;

    @ApiModelProperty(value = "工序6")
    private String process6;

    @ApiModelProperty(value = "工序7")
    private String process7;

    @ApiModelProperty(value = "生产信息详情ID")
    private Long produceDataItemId;

    @ApiModelProperty(value = "供应商")
    private String supplierCodeJoining;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "原料产品名称")
    private String rawSkuEncode;

    @ApiModelProperty(value = "优先级")
    private Integer sort;

    @ApiModelProperty(value = "商品二级类目名称")
    private String categoryName;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductNameJoining;

    @ApiModelProperty(value = "商品采购价格")
    private String goodsPurchasePrice;

    @ApiModelProperty(value = "最新采购价格")
    private String latestGoodsPurchasePriceJoining;
}
