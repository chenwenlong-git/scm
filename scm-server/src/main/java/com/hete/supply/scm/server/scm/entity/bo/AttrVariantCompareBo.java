package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 属性与变体对应关系对象
 *
 * @author yanjiawei
 * Created on 2024/11/14.
 */
@Data
public class AttrVariantCompareBo {
    @ApiModelProperty(value = "属性名ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性名")
    private String attributeName;

    @ApiModelProperty(value = "属性值ID")
    private Long attributeValueId;

    @ApiModelProperty(value = "属性值")
    private String attributeValue;

    @ApiModelProperty(value = "变体名")
    private String variantName;

    @ApiModelProperty(value = "变体属性值")
    private String variantValue;
}
