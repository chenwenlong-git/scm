package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/4 14:11
 */
@Data
@NoArgsConstructor
public class DevelopChildOrderAttrVo {

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;
}
