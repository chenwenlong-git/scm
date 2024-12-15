package com.hete.supply.scm.server.supplier.purchase.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.server.supplier.enums.ReturnRelatedOrderType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 采购退货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_return_order")
@ApiModel(value = "PurchaseReturnOrderPo对象", description = "采购退货单")
public class PurchaseReturnOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_return_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseReturnOrderId;


    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;


    @ApiModelProperty(value = "退货单状态")
    private ReturnOrderStatus returnOrderStatus;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "退货单来源单据号")
    private String returnBizNo;


    @ApiModelProperty(value = "退货信息确认人")
    private String returnUser;


    @ApiModelProperty(value = "退货信息确认人")
    private String returnUsername;

    @ApiModelProperty(value = "退货时间")
    private LocalDateTime returnTime;


    @ApiModelProperty(value = "物流")
    private String logistics;


    @ApiModelProperty(value = "运单号")
    private String trackingNo;


    @ApiModelProperty(value = "预计退货总数")
    private Integer expectedReturnCnt;

    @ApiModelProperty(value = "实际退货总数")
    private Integer realityReturnCnt;

    @ApiModelProperty(value = "收货总数")
    private Integer receiptCnt;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "收货人id")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "退货类型")
    private ReturnType returnType;

    @ApiModelProperty(value = "关联单据号")
    private String relatedBizNo;

    @ApiModelProperty(value = "关联单据类型")
    private ReturnRelatedOrderType relatedBizType;

    @ApiModelProperty(value = "关联单据创建时间")
    private LocalDateTime relatedBizTime;

    @ApiModelProperty(value = "退货单备注")
    private String note;

    @ApiModelProperty(value = "退货信息确认人")
    private String returnCreateUser;

    @ApiModelProperty(value = "退货信息确认人")
    private String returnCreateUsername;

    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;
}
