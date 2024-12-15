package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购需求母单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_parent_order_item")
@ApiModel(value = "PurchaseParentOrderItemPo对象", description = "采购需求母单明细")
public class PurchaseParentOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_parent_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseParentOrderItemId;


    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;


    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "采购未交数")
    private Integer undeliveredCnt;


    @ApiModelProperty(value = "采购可拆单数")
    private Integer canSplitCnt;
}
