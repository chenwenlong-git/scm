package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseDemandType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseParentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SkuType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 采购需求母单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_parent_order")
@ApiModel(value = "PurchaseParentOrderPo对象", description = "采购需求母单")
public class PurchaseParentOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_parent_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseParentOrderId;


    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku类型")
    private SkuType skuType;


    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;


    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;


    @ApiModelProperty(value = "采购单状态")
    private PurchaseParentOrderStatus purchaseParentOrderStatus;


    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;

    @ApiModelProperty(value = "采购总数")
    private Integer purchaseTotal;


    @ApiModelProperty(value = "采购单备注")
    private String orderRemarks;


    @ApiModelProperty(value = "是否首单")
    private PurchaseOrderType purchaseOrderType;


    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;


    @ApiModelProperty(value = "是否正常采购")
    private BooleanType isNormalOrder;


    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;

    @ApiModelProperty(value = "是否导入生成")
    private BooleanType isImportation;

    @ApiModelProperty(value = "采购可拆单数")
    private Integer canSplitCnt;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "采购需求类型")
    private PurchaseDemandType purchaseDemandType;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;

    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;

}
