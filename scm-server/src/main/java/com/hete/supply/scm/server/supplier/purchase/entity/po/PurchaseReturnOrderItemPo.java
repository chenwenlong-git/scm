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

import java.math.BigDecimal;

/**
 * <p>
 * 采购退货单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_return_order_item")
@ApiModel(value = "PurchaseReturnOrderItemPo对象", description = "采购退货单明细")
public class PurchaseReturnOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_return_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseReturnOrderItemId;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "退货单来源单据号")
    private String returnBizNo;

    @ApiModelProperty(value = "实际退货数量")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "预计退货数量")
    private Integer expectedReturnCnt;

    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "需扣款金额")
    private BigDecimal deductPrice;

    @ApiModelProperty(value = "质检明细id")
    private Long bizDetailId;


}
