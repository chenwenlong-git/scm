package com.hete.supply.scm.server.supplier.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.RawReceiptBizType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 采购原料收货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_raw_receipt_order")
@ApiModel(value = "PurchaseRawReceiptOrderPo对象", description = "采购原料收货单")
public class PurchaseRawReceiptOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_raw_receipt_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseRawReceiptOrderId;


    @ApiModelProperty(value = "采购原料收货单号")
    private String purchaseRawReceiptOrderNo;


    @ApiModelProperty(value = "收货单状态")
    private ReceiptOrderStatus receiptOrderStatus;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "发货数量")
    private Integer deliverCnt;


    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;


    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;


    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "采购原料发货单号")
    private String purchaseRawDeliverOrderNo;

    @ApiModelProperty(value = "原料收货类型")
    private RawReceiptBizType rawReceiptBizType;

    @ApiModelProperty(value = "样品子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;
}
