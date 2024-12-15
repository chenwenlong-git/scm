package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author weiwenxin
 * @date 2023/8/29 14:18
 */
@Data
@NoArgsConstructor
public class PurchaseChildDeliverExportVo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private String purchaseOrderStatus;

    @ApiModelProperty(value = "约定交期")
    private String deliverDate;

    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private String deliverOrderStatus;

    @ApiModelProperty(value = "发货单类型")
    private String deliverOrderType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private String deliverTime;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "总采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;


    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "上架数")
    private Integer receiveAmount;

    @ApiModelProperty(value = "收货时间")
    private String receiptTime;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "收货单状态")
    private String receiveOrderStateName;

    @ApiModelProperty(value = "入库时间")
    private String wmsWarehousingTime;
}
