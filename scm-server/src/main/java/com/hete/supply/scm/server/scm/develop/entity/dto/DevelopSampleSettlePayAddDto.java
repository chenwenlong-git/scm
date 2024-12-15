package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 17:08
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettlePayAddDto {

    @NotNull(message = "样品结算单ID不能为空")
    @ApiModelProperty(value = "样品结算单ID")
    private Long developSampleSettleOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "交易号不能为空")
    @Length(max = 32, message = "交易号字符长度不能超过 32 位")
    @ApiModelProperty(value = "交易号")
    private String transactionNo;

    @NotNull(message = "支付金额不能为空")
    @Min(value = 0, message = "支付金额不能小于等于0")
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payPrice;

    @NotNull(message = "支付时间不能为空")
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @NotEmpty(message = "支付凭证不能为空")
    @ApiModelProperty(value = "支付凭证")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "备注字符长度不能超过 255 位")
    private String remarks;

}
