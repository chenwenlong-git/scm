package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
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
 * 预付款单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_prepayment_order_item")
@ApiModel(value = "FinancePrepaymentOrderItemPo对象", description = "预付款单明细")
public class FinancePrepaymentOrderItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_prepayment_order_item_id", type = IdType.ASSIGN_ID)
    private Long financePrepaymentOrderItemId;


    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;


    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;


    @ApiModelProperty(value = "收款账户")
    private String account;

    @ApiModelProperty(value = "主体")
    private String subject;

    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;


    @ApiModelProperty(value = "银行账号")
    private String bankName;


    @ApiModelProperty(value = "户名")
    private String accountUsername;


    @ApiModelProperty(value = "银行支行")
    private String bankSubbranchName;

    @ApiModelProperty(value = "账号备注")
    private String accountRemarks;

    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;


    @ApiModelProperty(value = "预付金额")
    private BigDecimal prepaymentMoney;


    @ApiModelProperty(value = "币种")
    private Currency currency;

    @ApiModelProperty(value = "目标收款金额")
    private BigDecimal targetPrepaymentMoney;


    @ApiModelProperty(value = "期望收款时间")
    private LocalDateTime expectedPrepaymentDate;


}
