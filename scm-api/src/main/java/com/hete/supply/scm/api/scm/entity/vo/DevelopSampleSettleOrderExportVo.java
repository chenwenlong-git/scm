package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class DevelopSampleSettleOrderExportVo {

    @ApiModelProperty(value = "样品结算单号")
    private String developSampleSettleOrderNo;

    @ApiModelProperty(value = "状态")
    private DevelopSampleSettleStatus developSampleSettleStatus;

    @ApiModelProperty(value = "状态名称")
    private String developSampleSettleStatusName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "已支付金额")
    private BigDecimal paidPrice;

    @ApiModelProperty(value = "待支付金额")
    private BigDecimal waitPayPrice;

    @ApiModelProperty(value = "单据总数")
    private Integer itemTotal;

    @ApiModelProperty(value = "对账人")
    private String confirmUsername;

    @ApiModelProperty(value = "供应商确认人")
    private String examineUsername;

    @ApiModelProperty(value = "财务审核人")
    private String settleUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "对账时间")
    private String confirmTimeStr;

    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;

    @ApiModelProperty(value = "供应商确认时间")
    private String examineTimeStr;

    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "财务审核时间")
    private String settleTimeStr;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "支付完成时间")
    private String payTimeStr;

    @ApiModelProperty(value = "供应商拒绝原因")
    private String settleRefuseRemarks;

    @ApiModelProperty(value = "财务拒绝原因")
    private String examineRefuseRemarks;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime itemSettleTime;

    @ApiModelProperty(value = "上架时间")
    private String itemSettleTimeStr;

    @ApiModelProperty(value = "收货单号")
    private String businessNo;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

}
