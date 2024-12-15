package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DiscountType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 采购需求子单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_child_order_item")
@ApiModel(value = "PurchaseChildOrderItemPo对象", description = "采购需求子单明细")
public class PurchaseChildOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_child_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseChildOrderItemId;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;


    @ApiModelProperty(value = "采购数(若有降档需求，则与初始采购数不同)")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "初始采购数")
    private Integer initPurchaseCnt;


    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;


    @ApiModelProperty(value = "优惠类型")
    private DiscountType discountType;


    @ApiModelProperty(value = "扣减金额")
    private BigDecimal substractPrice;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;
}
