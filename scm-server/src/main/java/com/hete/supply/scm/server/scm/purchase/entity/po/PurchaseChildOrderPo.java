package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 采购需求子单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_child_order")
@ApiModel(value = "PurchaseChildOrderPo对象", description = "采购需求子单")
public class PurchaseChildOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_child_order_id", type = IdType.ASSIGN_ID)
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


    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "需求对象")
    private SkuType skuType;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "拆分类型")
    private PurchaseBizType purchaseBizType;


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


    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;


    @ApiModelProperty(value = "总采购数量")
    private Integer purchaseTotal;


    @ApiModelProperty(value = "要求发货时间")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "总结算金额")
    private BigDecimal totalSettlePrice;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "是否上传海外仓文件信息")
    private BooleanType isUploadOverseasMsg;

    @ApiModelProperty(value = "是否导入生成")
    private BooleanType isImportation;

    @ApiModelProperty(value = "原料是否剩余")
    private BooleanType rawRemainTab;

    @ApiModelProperty(value = "源采购子单单号")
    private String sourcePurchaseChildOrderNo;

    @ApiModelProperty(value = "是否超期")
    private BooleanType isOverdue;

    @ApiModelProperty(value = "期望上架时间")
    private LocalDateTime expectedOnShelvesDate;

    @ApiModelProperty(value = "准交数")
    private Integer timelyDeliveryCnt;

    @ApiModelProperty(value = "准交率")
    private BigDecimal timelyDeliveryRate;

    @ApiModelProperty(value = "最后准交时间")
    private LocalDateTime timelyDeliveryTime;

    @ApiModelProperty(value = "可发货数")
    private Integer shippableCnt;

    @ApiModelProperty(value = "终止来货备注")
    private String finishRemark;

    @ApiModelProperty(value = "编辑后延期天数")
    private Integer delayDays;

    @ApiModelProperty(value = "拆单补交类型")
    private SplitType splitType;

    @ApiModelProperty(value = "答交时间")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "最新调价审批单号")
    private String adjustPriceApproveNo;


    @ApiModelProperty(value = "供应商产能")
    private BigDecimal capacity;

    @ApiModelProperty(value = "下单方式")
    private OrderSource orderSource;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

    @ApiModelProperty(value = "答交时间是否变更")
    private BooleanType promiseDateChg;

    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;
}
