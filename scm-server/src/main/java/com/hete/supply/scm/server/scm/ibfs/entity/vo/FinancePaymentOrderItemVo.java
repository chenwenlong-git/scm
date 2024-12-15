package com.hete.supply.scm.server.scm.ibfs.entity.vo;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/13 09:40
 */
@Data
@NoArgsConstructor
public class FinancePaymentOrderItemVo {
    @ApiModelProperty(value = "付款主体")
    private String paymentSubject;

    @ApiModelProperty(value = "付款事由")
    private String paymentReason;

    @ApiModelProperty(value = "付款备注说明")
    private String paymentRemark;

    @ApiModelProperty(value = "凭证附件")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "收方主体")
    private String subject;

    @ApiModelProperty(value = "收款账户(银行账号)")
    private String account;

    @ApiModelProperty(value = "账号银行(开户银行)")
    private String bankName;

    @ApiModelProperty(value = "银行支行(支行名称)")
    private String bankSubbranchName;

    @ApiModelProperty(value = "银行所在地区 省份/州")
    private String bankProvince;

    @ApiModelProperty(value = "银行所在地区 城市")
    private String bankCity;

    @ApiModelProperty(value = "银行所在地区 区")
    private String bankArea;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentMoney;

    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @ApiModelProperty(value = "汇率转换人民币付款金额")
    private BigDecimal rmbPaymentMoney;

    @ApiModelProperty(value = "币种")
    private Currency currency;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime paymentDate;

    @ApiModelProperty(value = "收款账户名称")
    private String accountUsername;
}
