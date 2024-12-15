package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import com.hete.supply.scm.server.scm.ibfs.enums.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@NoArgsConstructor
@ApiModel(description = "新增付款记录请求数据传输对象")
public class AddPaymentRecordDto {

    @ApiModelProperty(value = "结算单号", required = true, example = "SETTLE123456")
    @NotBlank(message = "结算单号不能为空")
    private String settleOrderNo;

    @ApiModelProperty(value = "版本号", required = true, example = "v1")
    @NotNull(message = "版本号不能为空")
    private Integer version;

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
    @DecimalMin(value = "0.01", message = "付款金额必须大于0")
    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentMoney;

    @NotNull(message = "目标付款金额不能为空")
    @DecimalMin(value = "0.01", message = "目标付款金额必须大于0")
    @ApiModelProperty(value = "目标付款金额")
    private BigDecimal targetPaymentMoney;

    @NotNull(message = "币种不能为空")
    @ApiModelProperty(value = "币种")
    private Currency currency;

    @NotNull(message = "汇率不能为空")
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "附件")
    private List<String> fileCodeList;
}
