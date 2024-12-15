package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/10/12 16:14
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrBo {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;

}
