package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/26 17:51
 */
@Data
@NoArgsConstructor
public class PurchaseSkuAndSupplierDto {
    @ApiModelProperty(value = "sku与供应商项")
    @Valid
    @NotEmpty(message = "sku与供应商列表不能为空")
    private List<PurchaseSkuAndSupplierItemDto> purchaseSkuAndSupplierItemList;
}
