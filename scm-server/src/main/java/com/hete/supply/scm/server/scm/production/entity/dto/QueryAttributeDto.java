package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryAttributeDto extends ComPageDto {
    @ApiModelProperty(value = "属性id")
    private String attrId;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性分类id", notes = "属性类型id")
    private Long attrCategoryId;

    @ApiModelProperty(value = "状态")
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "录入类型")
    private AttributeInputType attributeInputType;

    @ApiModelProperty(value = "选项属性值")
    private String attributeValue;
}
