package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/18.
 */
@Data
public class SupSkuAttrValueVo {
    @ApiModelProperty(value = "供应商商品属性值主键id")
    private Long supSkuAttrValueId;

    @ApiModelProperty(value = "供应商商品属性属性值关联id")
    private Long valueId;

    @ApiModelProperty(value = "供应商商品属性属性值")
    private String value;
}
