package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.PurchaseBizType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.support.api.enums.BooleanType;
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
public class PurchaseChildSkuExportVo {

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

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商产品名称")
    private String supplierProductName;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "优惠类型")
    private String discountType;

    @ApiModelProperty(value = "扣减金额")
    private BigDecimal substractPrice;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime parentCreateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime childCreateTime;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;

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

    @ApiModelProperty(value = "质检完成时间")
    private LocalDateTime qcTime;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;


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

    @ApiModelProperty(value = "源采购子单单号")
    private String sourcePurchaseChildOrderNo;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

    @ApiModelProperty(value = "采购类型")
    private PurchaseBizType purchaseBizType;

    @ApiModelProperty(value = "订单类型")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTime;

    @ApiModelProperty(value = "答交时间是否变更")
    private BooleanType promiseDateChg;

    @ApiModelProperty(value = "答交时间是否变更")
    private String promiseDateChgStr;
}
