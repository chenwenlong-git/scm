package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@NoArgsConstructor
@ApiModel(description = "新增结算单收款账户请求数据传输对象")
public class AddSettleOrderAccountDto {
    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号", required = true, example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "版本号", required = true, example = "v1")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @ApiModelProperty(value = "银行账号", required = true)
    @NotBlank(message = "银行账号不能为空")
    private String account;

    @ApiModelProperty(value = "预付申请金额", required = true)
    @NotNull(message = "预付申请金额不能为空")
    @DecimalMin(value = "0.01", message = "预付申请金额必须大于0")
    @JsonProperty("targetPrepaymentMoney")
    private BigDecimal expectReceiveAmount;

    @ApiModelProperty(value = "期望收款时间", required = true)
    @NotNull(message = "期望收款时间不能为空")
    private LocalDateTime expectReceiveDate;
}
