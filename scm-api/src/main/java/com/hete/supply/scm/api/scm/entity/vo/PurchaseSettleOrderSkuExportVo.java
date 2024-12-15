package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class PurchaseSettleOrderSkuExportVo {

    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;


    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "单据类型")
    private String purchaseSettleItemTypeName;


    @ApiModelProperty(value = "单据号")
    private String businessNo;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "单据状态")
    private String purchaseSettleItemStatusName;

    @ApiModelProperty(value = "关联采购子单")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单类型")
    private String purchaseBizTypeName;


    @ApiModelProperty(value = "单据时间")
    private LocalDateTime itemSettleTime;


    @ApiModelProperty(value = "单据总结算金额")
    private BigDecimal purchaseSettleItemSettlePrice;


    @ApiModelProperty(value = "补/扣款原因")
    private String supplementDeductRemarks;


    @ApiModelProperty(value = "结算价（行）")
    private BigDecimal totalSettlePrice;


    @ApiModelProperty(value = "收货仓库")
    private String warehouseName;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;


    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "结算单价")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "收货单号")
    private String purchaseReceiptOrderNo;

    @ApiModelProperty(value = "收货单状态")
    private String receiveOrderStateName;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryAmount;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveAmount;

    @ApiModelProperty(value = "退货数")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "已上架数量")
    private Integer onShelvesAmount;

}
