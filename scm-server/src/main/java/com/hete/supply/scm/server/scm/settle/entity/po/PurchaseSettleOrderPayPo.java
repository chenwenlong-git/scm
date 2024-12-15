package com.hete.supply.scm.server.scm.settle.entity.po;

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
import java.time.LocalDateTime;

/**
 * <p>
 * 采购结算单支付明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_settle_order_pay")
@ApiModel(value = "PurchaseSettleOrderPayPo对象", description = "采购结算单支付明细")
public class PurchaseSettleOrderPayPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "purchase_settle_order_pay_id", type = IdType.ASSIGN_ID)
    private Long purchaseSettleOrderPayId;


    @ApiModelProperty(value = "关联结算单ID")
    private Long purchaseSettleOrderId;


    @ApiModelProperty(value = "采购结算单号")
    private String purchaseSettleOrderNo;


    @ApiModelProperty(value = "交易号")
    private String transactionNo;


    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;


    @ApiModelProperty(value = "支付金额")
    private BigDecimal payPrice;


    @ApiModelProperty(value = "备注")
    private String remarks;


}
