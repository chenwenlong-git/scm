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
 * 采购需求子单变更记录
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_child_order_change")
@ApiModel(value = "PurchaseChildOrderChangePo对象", description = "采购需求子单变更记录")
public class PurchaseChildOrderChangePo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_child_order_change_id", type = IdType.ASSIGN_ID)
    private Long purchaseChildOrderChangeId;


    @ApiModelProperty(value = "采购子单id")
    private Long purchaseChildOrderId;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


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


    @ApiModelProperty(value = "wms收货时间")
    private LocalDateTime wmsReceiptTime;


    @ApiModelProperty(value = "wms质检时间")
    private LocalDateTime wmsQcTime;


    @ApiModelProperty(value = "wms入库时间")
    private LocalDateTime wmsWarehousingTime;


    @ApiModelProperty(value = "wms退货时间")
    private LocalDateTime wmsReturnTime;


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

    @ApiModelProperty(value = "最后修改期望上架时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value = "确认人")
    private String confirmUser;

    @ApiModelProperty(value = "确认人")
    private String confirmUsername;


    @ApiModelProperty(value = "收货人")
    private String receiptUser;

    @ApiModelProperty(value = "收货人")
    private String receiptUsername;


    @ApiModelProperty(value = "质检人")
    private String qcUser;

    @ApiModelProperty(value = "质检人")
    private String qcUsername;


    @ApiModelProperty(value = "接单人")
    private String receiveOrderUser;

    @ApiModelProperty(value = "接单人")
    private String receiveOrderUsername;

    @ApiModelProperty(value = "入库人")
    private String warehousingUser;

    @ApiModelProperty(value = "入库人")
    private String warehousingUsername;

    @ApiModelProperty(value = "最后修改期望上架时间人")
    private String lastModifyUser;

    @ApiModelProperty(value = "最后修改期望上架时间人")
    private String lastModifyUsername;

    @ApiModelProperty(value = "发货单号")
    private String purchaseDeliverOrderNo;


    @ApiModelProperty(value = "收货单号")
    private String purchaseReceiptOrderNo;


    @ApiModelProperty(value = "退货单号")
    private String purchaseReturnOrderNo;


    @ApiModelProperty(value = "结算单号")
    private String purchaseSettleOrderNo;

    @ApiModelProperty(value = "计划确认人")
    private String planConfirmUser;

    @ApiModelProperty(value = "计划确认人")
    private String planConfirmUsername;

    @ApiModelProperty(value = "计划确认时间")
    private LocalDateTime planConfirmTime;

}
