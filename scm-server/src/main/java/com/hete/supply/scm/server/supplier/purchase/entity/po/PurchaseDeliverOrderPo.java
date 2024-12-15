package com.hete.supply.scm.server.supplier.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeliverOrderType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 采购发货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_deliver_order")
@ApiModel(value = "PurchaseDeliverOrderPo对象", description = "采购发货单")
public class PurchaseDeliverOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_deliver_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseDeliverOrderId;


    @ApiModelProperty(value = "采购发货单号")
    private String purchaseDeliverOrderNo;

    @ApiModelProperty(value = "采购收货单号")
    private String purchaseReceiptOrderNo;

    @ApiModelProperty(value = "发货单状态")
    private DeliverOrderStatus deliverOrderStatus;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;


    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "采购约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "是否生成箱唛")
    private BooleanType hasShippingMark;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime warehousingTime;

    @ApiModelProperty(value = "发货单类型")
    private DeliverOrderType deliverOrderType;
}
