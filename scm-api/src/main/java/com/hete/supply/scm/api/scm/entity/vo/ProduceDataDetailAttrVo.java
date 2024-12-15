package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/10/17 13:50
 */
@Data
@NoArgsConstructor
public class ProduceDataDetailAttrVo {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "key")
    private String attrName;

    @ApiModelProperty(value = "value")
    private String attrValue;

}
