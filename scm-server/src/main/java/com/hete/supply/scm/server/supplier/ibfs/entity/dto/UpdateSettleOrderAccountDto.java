package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

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
@ApiModel(description = "更新结算单收款账户请求数据传输对象")
@NoArgsConstructor
public class UpdateSettleOrderAccountDto {
    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号", required = true, example = "SETTLE123456")
    private String settleOrderNo;

    @ApiModelProperty(value = "版本号", required = true, example = "v1")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @NotNull(message = "收款信息主键ID不能为空")
    @ApiModelProperty(value = "收款信息主键ID", required = true, example = "v1")
    private Long financeSettleOrderReceiveId;

    @ApiModelProperty(value = "收款信息版本号", required = true, example = "v1")
    @NotNull(message = "收款信息版本号不能为空")
    private Integer financeSettleOrderReceiveVersion;

    @ApiModelProperty(value = "银行账号", required = true)
    @NotBlank(message = "银行账号不能为空")
    private String account;

    @ApiModelProperty(value = "收款金额", example = "100.00")
    @DecimalMin(value = "0.01", message = "收款金额必须大于0")
    private BigDecimal currencyAmount;

    @ApiModelProperty(value = "期望收款时间", required = true)
    @NotNull(message = "期望收款时间不能为空")
    private LocalDateTime expectReceiveDate;
}
