package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/22 14:45
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrVo {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;

}
