package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/12 00:00
 */
@Data
@NoArgsConstructor
public class SampleChildExportVo {
    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private String sampleOrderStatus;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;

    @ApiModelProperty(value = "打样单价")
    private BigDecimal proofingPrice;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "收货人")
    private String receiptUsername;


    @ApiModelProperty(value = "选样人")
    private String sampleUsername;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "母单创建时间")
    private LocalDateTime parentOrderCreateTime;

    @ApiModelProperty(value = "子单创建时间")
    private LocalDateTime childOrderCreateTime;

    @ApiModelProperty(value = "子单备注")
    private String demandDescribe;

}
