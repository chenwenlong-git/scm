package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/18 17:48
 */
@Data
@NoArgsConstructor
public class PurchaseSkuSupplierDto {

    @ApiModelProperty(value = "供应商sku信息")
    @NotEmpty(message = "供应商sku信息不能为空")
    @Valid
    private List<PurchaseSkuSupplierItemDto> purchaseSkuSupplierItemList;
}
