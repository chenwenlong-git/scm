package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.PrepaymentType;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.DeductionStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 预付款单
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_prepayment_order")
@ApiModel(value = "FinancePrepaymentOrderPo对象", description = "预付款单")
public class FinancePrepaymentOrderPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_prepayment_order_id", type = IdType.ASSIGN_ID)
    private Long financePrepaymentOrderId;


    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;


    @ApiModelProperty(value = "预付款单状态")
    private PrepaymentOrderStatus prepaymentOrderStatus;


    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;


    @ApiModelProperty(value = "预付款类型：")
    private PrepaymentType prepaymentType;


    @ApiModelProperty(value = "预付款事由")
    private String prepaymentReason;


    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentMoney;

    @ApiModelProperty(value = "币种")
    private Currency currency;


    @ApiModelProperty(value = "付款金额(rmb)")
    private BigDecimal paymentMoney;


    @ApiModelProperty(value = "飞书审批单号")
    private String workflowNo;


    @ApiModelProperty(value = "可抵扣金额(rmb)")
    private BigDecimal canDeductionMoney;


    @ApiModelProperty(value = "抵扣状态")
    private DeductionStatus deductionStatus;

    @ApiModelProperty(value = "申请日期")
    private LocalDateTime applyDate;

    @ApiModelProperty(value = "预付款备注")
    private String prepaymentRemark;

    @ApiModelProperty(value = "当前操作人")
    private String ctrlUser;

    @ApiModelProperty(value = "审批taskId")
    private String taskId;

    @ApiModelProperty(value = "跟单")
    private String followUser;

    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;
}
