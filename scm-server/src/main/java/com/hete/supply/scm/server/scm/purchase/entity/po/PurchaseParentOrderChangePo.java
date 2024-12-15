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

import java.time.LocalDateTime;

/**
 * <p>
 * 采购需求母单变更记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_parent_order_change")
@ApiModel(value = "PurchaseParentOrderChangePo对象", description = "采购需求母单变更记录")
public class PurchaseParentOrderChangePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_parent_order_change_id", type = IdType.ASSIGN_ID)
    private Long purchaseParentOrderChangeId;


    @ApiModelProperty(value = "采购母单id")
    private Long purchaseParentOrderId;

    @ApiModelProperty(value = "采购母单单号")
    private String purchaseParentOrderNo;


    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approveTime;


    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;


    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTime;


    @ApiModelProperty(value = "排产时间")
    private LocalDateTime schedulingTime;


    @ApiModelProperty(value = "投产时间")
    private LocalDateTime commissioningTime;


    @ApiModelProperty(value = "前处理时间")
    private LocalDateTime pretreatmentTime;


    @ApiModelProperty(value = "缝制中时间")
    private LocalDateTime sewingTime;


    @ApiModelProperty(value = "后处理时间")
    private LocalDateTime aftertreatmentTime;


    @ApiModelProperty(value = "下单时间")
    private LocalDateTime placeOrderTime;


    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTime;


    @ApiModelProperty(value = "接单时间")
    private LocalDateTime receiveOrderTime;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "质检时间")
    private LocalDateTime qcTime;


    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;


    @ApiModelProperty(value = "审核人")
    private String approveUser;


    @ApiModelProperty(value = "审核人")
    private String approveUsername;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUser;


    @ApiModelProperty(value = "下单人")
    private String placeOrderUsername;


}
