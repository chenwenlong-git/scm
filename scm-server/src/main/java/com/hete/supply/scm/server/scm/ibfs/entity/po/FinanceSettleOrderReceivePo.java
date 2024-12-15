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
 * 对账单收款明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("finance_settle_order_receive")
@ApiModel(value = "FinanceSettleOrderReceivePo对象", description = "对账单收款明细")
public class FinanceSettleOrderReceivePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "finance_settle_order_receive_id", type = IdType.ASSIGN_ID)
    private Long financeSettleOrderReceiveId;


    @ApiModelProperty(value = "结算单号")
    private String financeSettleOrderNo;


    @ApiModelProperty(value = "供应商代码")
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


    @ApiModelProperty(value = "币种:RMB(人民币),USD(美元),")
    private Currency currency;


    @ApiModelProperty(value = "币种金额")
    private BigDecimal currencyAmount;


    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;


    @ApiModelProperty(value = "预计收款金额（人民币）")
    private BigDecimal expectReceiveAmount;


    @ApiModelProperty(value = "期望收款时间")
    private LocalDateTime expectReceiveDate;
}
