package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.ibfs.enums.FinanceSettleCarryoverOrderStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 财务结算结转单表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_settle_carryover_order")
@ApiModel(value = "FinanceSettleCarryoverOrderPo对象", description = "财务结算结转单表")
public class FinanceSettleCarryoverOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_settle_carryover_order_id", type = IdType.ASSIGN_ID)
    private Long financeSettleCarryoverOrderId;


    @ApiModelProperty(value = "结转单号")
    private String financeSettleCarryoverOrderNo;


    @ApiModelProperty(value = "结转单状态")
    private FinanceSettleCarryoverOrderStatus financeSettleCarryoverOrderStatus;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "结算单号")
    private String financeSettleOrderNo;


    @ApiModelProperty(value = "结转金额")
    private BigDecimal carryoverAmount;


    @ApiModelProperty(value = "可结转金额")
    private BigDecimal availableCarryoverAmount;
}
