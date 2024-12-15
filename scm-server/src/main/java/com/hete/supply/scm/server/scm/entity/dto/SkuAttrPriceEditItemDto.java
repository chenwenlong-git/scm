package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/9/10 11:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SkuAttrPriceEditItemDto extends SkuAttrItemDto {
    @ApiModelProperty(value = "id")
    private Long skuAttrPriceId;

    @ApiModelProperty(value = "version")
    private Integer version;


    @ApiModelProperty(value = "sku价格")
    private BigDecimal skuPrice;

}
