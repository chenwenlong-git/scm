package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.supply.scm.server.scm.purchase.enums.PurchaseReturnPlacedCnt;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/12/25 17:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseCancelDto extends PurchaseChildIdAndVersionDto {
    @ApiModelProperty(value = "采购是否返还sku未下单数")
    @NotNull(message = "采购是否返还sku未下单数不能为空")
    private PurchaseReturnPlacedCnt purchaseReturnPlacedCnt;
}
