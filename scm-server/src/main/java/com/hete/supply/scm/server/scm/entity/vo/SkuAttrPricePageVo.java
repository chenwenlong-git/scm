package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/9/10 09:35
 */
@Data
@NoArgsConstructor
public class SkuAttrPricePageVo {
    @ApiModelProperty(value = "id")
    private Long skuAttrPriceId;

    @ApiModelProperty(value = "蕾丝面积属性值")
    private String laceAttrValue;


    @ApiModelProperty(value = "档长尺寸属性值")
    private String sizeAttrValue;

    @ApiModelProperty(value = "材料属性值")
    private String materialAttrValue;

    @ApiModelProperty(value = "sku价格")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "version")
    private Integer version;
}
