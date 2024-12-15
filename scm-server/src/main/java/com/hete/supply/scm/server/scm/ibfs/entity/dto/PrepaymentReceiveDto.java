package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:46
 */
@Data
@NoArgsConstructor
public class PrepaymentReceiveDto {
    @NotBlank(message = "预付款单号不能为空")
    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @NotBlank(message = "收款账户不能为空")
    @ApiModelProperty(value = "收款账户")
    private String account;

    @JsonProperty(value = "currencyAmount")
    @NotNull(message = "收款金额不能为空")
    @ApiModelProperty(value = "收款金额")
    private BigDecimal prepaymentMoney;

    @JsonProperty(value = "expectReceiveDate")
    @NotNull(message = "期望收款时间不能为空")
    @ApiModelProperty(value = "期望收款时间")
    private LocalDateTime expectedPrepaymentDate;

    @NotNull(message = "目标收款金额不能为空")
    @ApiModelProperty(value = "目标收款金额（申请币种金额）")
    private BigDecimal targetPrepaymentMoney;
}
