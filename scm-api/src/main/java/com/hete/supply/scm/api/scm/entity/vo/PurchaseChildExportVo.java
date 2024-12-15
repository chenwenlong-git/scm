package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/12 14:58
 */
@Data
@NoArgsConstructor
public class PurchaseChildExportVo {

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private String purchaseOrderStatus;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "供应商")
    private String supplierCode;


    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "总采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "准交数")
    private Integer timelyDeliveryCnt;

    @ApiModelProperty(value = "准交率")
    private BigDecimal timelyDeliveryRate;

    @ApiModelProperty(value = "准交率")
    private String timelyDeliveryRateStr;

    @ApiModelProperty(value = "最后准交时间")
    private LocalDateTime timelyDeliveryTime;

    @ApiModelProperty(value = "最后修改期望上架时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "已退货数")
    private Integer returnCnt;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTime;

}
