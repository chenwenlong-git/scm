package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/6/6 10:58
 */
@Data
@NoArgsConstructor
public class PurchaseRawReceiptExportVo {
    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "采购原料发货单号")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "发货时间")
    private String deliverTime;

    @ApiModelProperty(value = "采购单状态")
    private String purchaseOrderStatus;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "原料仓库名称")
    private String rawWarehouseName;

    @ApiModelProperty(value = "原料sku")
    private String rawSku;

    @ApiModelProperty(value = "原料sku产品名称")
    private String rawSkuEncode;

    @ApiModelProperty(value = "单位bom需求")
    private Integer bomDeliveryCnt;

    @ApiModelProperty(value = "出库数")
    private Integer deliveryCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "收货时间")
    private String receiptTime;

    @ApiModelProperty(value = "子单发货时间")
    private String purchaseDeliverTime;

    @ApiModelProperty(value = "子单收货时间")
    private String purchaseReceiptTime;

    @ApiModelProperty(value = "入库时间")
    private String warehousingTime;
}
