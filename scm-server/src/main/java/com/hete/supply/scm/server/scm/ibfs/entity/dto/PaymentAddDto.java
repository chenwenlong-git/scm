package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PaymentAddDto extends PrepaymentNoDto {
    @NotBlank(message = "预付款对象不能为空")
    @ApiModelProperty(value = "预付款对象(供应商code)")
    private String supplierCode;

    @NotBlank(message = "收款账户不能为空")
    @ApiModelProperty(value = "收款账户")
    private String account;

    @NotBlank(message = "付款主体不能为空")
    @ApiModelProperty(value = "付款主体")
    private String paymentSubject;

    @Length(max = 500, message = "付款事由超过500个字符")
    @NotBlank(message = "付款事由不能为空")
    @ApiModelProperty(value = "付款事由")
    private String paymentReason;

    @Length(max = 500, message = "付款备注说明超过500个字符")
    @ApiModelProperty(value = "付款备注说明")
    private String paymentRemark;

    @NotNull(message = "付款金额不能为空")
    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentMoney;

    @NotNull(message = "币种不能为空")
    @ApiModelProperty(value = "币种")
    private Currency currency;

    @NotNull(message = "目标汇率不能为空")
    @ApiModelProperty(value = "目标汇率")
    private BigDecimal exchangeRate;

    @NotNull(message = "目标付款金额不能为空")
    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @NotNull(message = "人民币汇率不能为空")
    @ApiModelProperty(value = "人民币汇率")
    private BigDecimal rmbExchangeRate;

    @NotNull(message = "人民币付款金额不能为空")
    @ApiModelProperty(value = "人民币付款金额")
    private BigDecimal rmbPaymentMoney;

    @ApiModelProperty(value = "附件")
    private List<String> fileCodeList;
}
