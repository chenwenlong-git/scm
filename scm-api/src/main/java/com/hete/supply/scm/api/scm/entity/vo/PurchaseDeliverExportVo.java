package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/12/18 17:34
 */
@Data
@NoArgsConstructor
public class PurchaseDeliverExportVo {
    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private String deliverOrderStatus;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库")
    private String warehouseName;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;


    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "收货单状态")
    private String receiveOrderStateName;

    @ApiModelProperty(value = "上架数")
    private Integer receiveAmount;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime wmsWarehousingTime;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "发货单类型")
    private DeliverOrderType deliverOrderType;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "结算总金额")
    private BigDecimal totalSettlePrice;

}
