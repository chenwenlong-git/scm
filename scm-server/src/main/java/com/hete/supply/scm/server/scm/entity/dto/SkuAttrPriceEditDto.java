package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/10 11:12
 */
@Data
@NoArgsConstructor
public class SkuAttrPriceEditDto {
    @NotEmpty(message = "sku定价明细不能为空")
    @ApiModelProperty(value = "sku定价明细")
    private List<SkuAttrPriceEditItemDto> skuAttrPriceEditItemList;
}
