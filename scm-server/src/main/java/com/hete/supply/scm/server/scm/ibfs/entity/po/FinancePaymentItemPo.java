package com.hete.supply.scm.server.scm.ibfs.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.ibfs.enums.PaymentBizType;
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
 * 财务相关单付款明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_payment_item")
@ApiModel(value = "FinancePaymentItemPo对象", description = "财务相关单付款明细")
public class FinancePaymentItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_payment_item_id", type = IdType.ASSIGN_ID)
    private Long financePaymentItemId;


    @ApiModelProperty(value = "关联业务单号")
    private String paymentBizNo;


    @ApiModelProperty(value = "关联业务类型")
    private PaymentBizType paymentBizType;


    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;


    @ApiModelProperty(value = "收款账户")
    private String account;

    @ApiModelProperty(value = "主体")
    private String subject;

    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;


    @ApiModelProperty(value = "开户银行")
    private String bankName;


    @ApiModelProperty(value = "户名")
    private String accountUsername;


    @ApiModelProperty(value = "银行支行")
    private String bankSubbranchName;

    @ApiModelProperty(value = "账号备注")
    private String accountRemarks;

    @ApiModelProperty(value = "付款主体")
    private String paymentSubject;


    @ApiModelProperty(value = "付款事由")
    private String paymentReason;


    @ApiModelProperty(value = "付款备注说明")
    private String paymentRemark;


    @ApiModelProperty(value = "收方主体")
    private String recipientSubject;


    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentMoney;


    @ApiModelProperty(value = "币种")
    private Currency currency;


    @ApiModelProperty(value = "目标汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @ApiModelProperty(value = "人民币汇率")
    private BigDecimal rmbExchangeRate;

    @ApiModelProperty(value = "汇率转换人民币付款金额")
    private BigDecimal rmbPaymentMoney;


    @ApiModelProperty(value = "付款时间")
    private LocalDateTime paymentDate;


    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;


}
