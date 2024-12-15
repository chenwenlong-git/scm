package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class PurchaseSettleOrderPayDelDto {

    @NotNull(message = "支付明细ID不能为空")
    @ApiModelProperty(value = "支付明细ID")
    private Long purchaseSettleOrderPayId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

}
