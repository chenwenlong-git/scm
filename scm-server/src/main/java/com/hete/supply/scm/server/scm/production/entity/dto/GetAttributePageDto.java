package com.hete.supply.scm.server.scm.production.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "供应链属性分页查询")
public class GetAttributePageDto extends ComPageDto {

    @ApiModelProperty(value = "属性主键id列表")
    private List<Long> attrIdList;

    @ApiModelProperty(value = "属性名称")
    private String attrName;

    @ApiModelProperty(value = "属性选项值")
    private String attributeValue;

    @ApiModelProperty(value = "一级属性分类id", notes = "属性类型")
    private Long firstAttrCategoryId;

    @ApiModelProperty(value = "二级属性分类id", notes = "SCM供应链属性类型")
    private Long secondAttrCategoryId;

    @JsonIgnore
    @ApiModelProperty(value = "二级属性分类id列表", notes = "SCM供应链属性类型")
    private List<Long> secondAttrCategoryIdList;

    @ApiModelProperty(value = "状态")
    private AttributeStatus attributeStatus;

    @ApiModelProperty(value = "录入类型")
    private AttributeInputType attributeInputType;
}
