package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 属性值与变体值关联业务对象
 *
 * @author yanjiawei
 * Created on 2024/11/22.
 */
@Data
public class UpdateProduceDataAttrBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "属性名id")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性名")
    private String attributeName;

    @ApiModelProperty(value = "属性值id")
    private Long attributeValueId;

    @ApiModelProperty(value = "属性值")
    private String attributeValue;

    @ApiModelProperty(value = "变体名")
    private String variantName;

    @ApiModelProperty(value = "变体属性值")
    private String variantValue;
}
