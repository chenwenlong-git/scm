package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2024/9/24 14:34
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrSkuVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;

    @ApiModelProperty(value = "关联属性ID")
    private Long attributeNameId;
}
