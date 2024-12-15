package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/14 00:10
 */
@Data
@NoArgsConstructor
public class PurchaseReturnExportVo {
    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "退货单状态")
    private String returnOrderStatus;

    @ApiModelProperty(value = "类型")
    private String returnType;

    @ApiModelProperty(value = "来源单据号")
    private String returnBizNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "预计退货数量")
    private Integer expectedReturnCnt;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "物流")
    private String logistics;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "生成时间")
    private String relatedBizTimeAsString;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeAsString;

    @ApiModelProperty(value = "退货人名称")
    private String returnUsername;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTime;

    @ApiModelProperty(value = "退货时间")
    private String returnTimeAsString;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "收货时间")
    private String receiptTimeAsString;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "预计结算单价")
    private BigDecimal settleRecoOrderPrice;

    @ApiModelProperty(value = "预计结算总价")
    private BigDecimal settleRecoOrderPriceTotal;

}
