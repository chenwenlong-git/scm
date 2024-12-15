package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/6/17 17:35
 */
@Data
@NoArgsConstructor
public class PurchaseAdjustPriceDto {
    @NotEmpty(message = "调价明细不能为空")
    @Valid
    @ApiModelProperty(value = "调价明细")
    private List<PurchaseAdjustPriceItemDto> purchaseAdjustPriceItemList;

}
