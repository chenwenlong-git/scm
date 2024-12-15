package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:48
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrItemDto {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;


}
