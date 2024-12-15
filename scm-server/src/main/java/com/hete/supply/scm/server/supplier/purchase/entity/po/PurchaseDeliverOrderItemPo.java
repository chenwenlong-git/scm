package com.hete.supply.scm.server.supplier.purchase.entity.po;

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
 * 采购发货单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_deliver_order_item")
@ApiModel(value = "PurchaseDeliverOrderItemPo对象", description = "采购发货单明细")
public class PurchaseDeliverOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_deliver_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseDeliverOrderItemId;


    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku变体属性")
    private String variantProperties;

    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @ApiModelProperty(value = "正品数")
    private Integer qualityGoodsCnt;


    @ApiModelProperty(value = "次品数")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseCnt;

}
