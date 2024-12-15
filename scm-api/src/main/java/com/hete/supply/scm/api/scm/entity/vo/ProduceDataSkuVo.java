package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ChenWenLong
 * @date 2024/6/26 16:43
 */
@Data
@NoArgsConstructor
public class ProduceDataSkuVo {

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "克重")
    private BigDecimal weight;

    @ApiModelProperty(value = "公差")
    private BigDecimal tolerance;
}
