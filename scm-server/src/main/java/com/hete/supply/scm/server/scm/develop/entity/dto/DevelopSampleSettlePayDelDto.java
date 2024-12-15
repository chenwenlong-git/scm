package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/1 17:11
 */
@Data
@NoArgsConstructor
public class DevelopSampleSettlePayDelDto {

    @NotNull(message = "支付明细ID不能为空")
    @ApiModelProperty(value = "支付明细ID")
    private Long developSampleSettlePayId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

}
