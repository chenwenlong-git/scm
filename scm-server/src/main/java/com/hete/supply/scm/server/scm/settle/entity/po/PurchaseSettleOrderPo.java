package com.hete.supply.scm.server.scm.settle.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseSettleStatus;
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
 * 采购结算单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_settle_order")
@ApiModel(value = "PurchaseSettleOrderPo对象", description = "采购结算单")
public class PurchaseSettleOrderPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_settle_order_id", type = IdType.ASSIGN_ID)
    private Long purchaseSettleOrderId;


    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;


    @ApiModelProperty(value = "采购结算单状态")
    private PurchaseSettleStatus purchaseSettleStatus;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "月份")
    private String month;


    @ApiModelProperty(value = "对账金额")
    private BigDecimal totalPrice;


    @ApiModelProperty(value = "应扣款金额")
    private BigDecimal deductPrice;


    @ApiModelProperty(value = "应付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;


    @ApiModelProperty(value = "确认拒绝备注")
    private String settleRefuseRemarks;


    @ApiModelProperty(value = "审核拒绝备注")
    private String examineRefuseRemarks;


    @ApiModelProperty(value = "对账人的用户")
    private String confirmUser;


    @ApiModelProperty(value = "对账人的用户名")
    private String confirmUsername;


    @ApiModelProperty(value = "对账时间")
    private LocalDateTime confirmTime;


    @ApiModelProperty(value = "供应商确认人的用户")
    private String examineUser;


    @ApiModelProperty(value = "供应商确认人的用户名")
    private String examineUsername;


    @ApiModelProperty(value = "供应商确认时间")
    private LocalDateTime examineTime;


    @ApiModelProperty(value = "财务审核人的用户")
    private String settleUser;


    @ApiModelProperty(value = "财务审核人的用户名")
    private String settleUsername;


    @ApiModelProperty(value = "财务审核时间")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "支付人的用户")
    private String payUser;


    @ApiModelProperty(value = "支付人的用户名")
    private String payUsername;


    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime payTime;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
