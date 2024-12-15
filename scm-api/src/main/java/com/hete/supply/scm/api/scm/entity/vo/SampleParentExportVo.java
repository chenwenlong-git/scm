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
public class SampleParentExportVo {
    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品单状态")
    private String sampleOrderStatus;

    @ApiModelProperty(value = "开发类型")
    private String sampleDevType;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "采购预估价")
    private BigDecimal purchasePredictPrice;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;

    @ApiModelProperty(value = "开款人")
    private String disburseUsername;

    @ApiModelProperty(value = "开款时间")
    private LocalDateTime disbursementTime;

    @ApiModelProperty(value = "确认打版时间")
    private LocalDateTime typesettingTime;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "是否首单")
    private String isFirstOrder;

    @ApiModelProperty(value = "是否加急")
    private String isUrgentOrder;
}
