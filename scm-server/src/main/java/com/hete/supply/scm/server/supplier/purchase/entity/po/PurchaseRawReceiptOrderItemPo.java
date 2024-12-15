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
 * 采购原料收货单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_raw_receipt_order_item")
@ApiModel(value = "PurchaseRawReceiptOrderItemPo对象", description = "采购原料收货单明细")
public class PurchaseRawReceiptOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_raw_receipt_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseRawReceiptOrderItemId;


    @ApiModelProperty(value = "采购原料收货单号")
    private String purchaseRawReceiptOrderNo;


    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "发货数量")
    private Integer deliverCnt;


    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;


}
