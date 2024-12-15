package com.hete.supply.scm.server.scm.production.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/29.
 */
@Data
public class VerifyAttrValueBo {
    @ApiModelProperty(value = "供应链属性关联id")
    private Long attrOptId;

    @ApiModelProperty(value = "sku商品属性属性值")
    private String value;
}
