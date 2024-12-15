package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/9 16:24
 */
@Data
@NoArgsConstructor
public class SupplierAndSkuDto {

    @NotEmpty(message = "sku与供应商列表不能为空")
    @ApiModelProperty(value = "SKU与供应商")
    @Size(max = 30, message = "SKU与供应商入参数量不能超过30个")
    private List<SupplierAndSkuItemDto> supplierAndSkuItemList;
}
