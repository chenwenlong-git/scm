package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleItemType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 采购结算单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_settle_order_item")
@ApiModel(value = "PurchaseSettleOrderItemPo对象", description = "采购结算单明细")
public class PurchaseSettleOrderItemPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_settle_order_item_id", type = IdType.ASSIGN_ID)
    private Long purchaseSettleOrderItemId;


    @ApiModelProperty(value = "关联结算单ID")
    private Long purchaseSettleOrderId;


    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "关联单据类型：大货采购单(PRODUCT_PURCHASE)、加工采购单(PROCESS_PURCHASE)、补款单(REPLENISH)、扣款单(DEDUCT)，样品采购单(SAMPLE)")
    private PurchaseSettleItemType purchaseSettleItemType;


    @ApiModelProperty(value = "单据时间(入库时间)")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "SKU数量")
    private Integer skuNum;


    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty(value = "单据状态")
    private String statusName;


}
