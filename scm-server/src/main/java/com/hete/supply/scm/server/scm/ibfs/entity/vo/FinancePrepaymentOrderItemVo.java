package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/5/13 09:25
 */
@Data
@NoArgsConstructor
public class FinancePrepaymentOrderItemVo {
    @ApiModelProperty(value = "收款账户(银行账号)")
    private String account;

    @ApiModelProperty(value = "账号类型")
    private SupplierPaymentAccountType supplierPaymentAccountType;

    @ApiModelProperty(value = "账户名称")
    private String accountUsername;

    @ApiModelProperty(value = "账号银行(开户银行)")
    private String bankName;

    @ApiModelProperty(value = "银行支行(支行名称)")
    private String bankSubbranchName;

    @ApiModelProperty(value = "账号备注")
    private String accountRemarks;

    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;

    @JsonProperty(value = "currencyAmount")
    @ApiModelProperty(value = "收款金额")
    private BigDecimal prepaymentMoney;

    @ApiModelProperty(value = "币种")
    private Currency currency;

    @JsonProperty(value = "expectReceiveDate")
    @ApiModelProperty(value = "期望收款时间")
    private LocalDateTime expectedPrepaymentDate;

    @ApiModelProperty(value = "目标收款金额")
    private BigDecimal targetPrepaymentMoney;
}
