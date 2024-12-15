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
 * 采购需求变更单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_modify_order_item")
@ApiModel(value = "PurchaseModifyOrderItemPo对象", description = "采购需求变更单明细")
public class PurchaseModifyOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_modify_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseModifyOrderItemId;


    @ApiModelProperty(value = "降档退货单号")
    private String downReturnOrderNo;


    @ApiModelProperty(value = "原sku")
    private String sku;


    @ApiModelProperty(value = "原sku数量")
    private Integer purchaseCnt;


    @ApiModelProperty(value = "新sku")
    private String newSku;


    @ApiModelProperty(value = "新sku数量")
    private Integer newPurchaseCnt;


}
