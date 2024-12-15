package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.OrderSource;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/8/8 14:32
 */
@Data
@NoArgsConstructor
public class PurchaseVo {
    @ApiModelProperty(value = "主键id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "样品单子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "采购单状态")
    private PurchaseOrderStatus purchaseOrderStatus;

    @ApiModelProperty(value = "订单类型")
    private PurchaseOrderType purchaseOrderType;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;

    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "总采购数量")
    private Integer purchaseTotal;

    @ApiModelProperty(value = "源采购子单单号")
    private String sourcePurchaseChildOrderNo;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "准交数")
    private Integer timelyDeliveryCnt;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "供应商产能")
    private BigDecimal capacity;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "下单方式")
    private OrderSource orderSource;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;

    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderItemId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "初始采购数")
    private Integer initPurchaseCnt;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

}
