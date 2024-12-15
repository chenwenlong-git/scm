package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/19.
 */
@Data
public class AttributeOptionVo {
    @ApiModelProperty(value = "属性ID")
    private Long attributeId;

    @ApiModelProperty(value = "属性选项主键id")
    private Long attrOptionId;

    @ApiModelProperty(value = "属性选项值")
    private String attributeValue;
}
