package com.hete.supply.scm.server.scm.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购需求变更单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_modify_order")
@ApiModel(value = "PurchaseModifyOrderPo对象", description = "采购需求变更单")
public class PurchaseModifyOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_modify_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseModifyOrderId;


    @ApiModelProperty(value = "降档退货单")
    private String downReturnOrderNo;


    @ApiModelProperty(value = "采购子单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "降档退货单状态")
    private ReceiptOrderStatus downReturnOrderStatus;


}
