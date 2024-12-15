package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/6/24 17:13
 */
@Data
@NoArgsConstructor
public class PurchaseDefaultPriceDto {
    @Valid
    @NotEmpty(message = "获取默认价格明细列表不能为空")
    @ApiModelProperty(value = "获取默认价格明细列表")
    private List<PurchaseDefaultPriceItemDto> purchaseDefaultPriceItemList;
}
