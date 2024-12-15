package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/18.
 */
@Data
public class SkuAttrValueVo {
    @ApiModelProperty(value = "sku属性值主键id")
    private Long skuAttrValueId;

    @ApiModelProperty(value = "sku商品属性属性值关联id")
    private Long valueId;

    @ApiModelProperty(value = "sku商品属性属性值")
    private String value;
}
