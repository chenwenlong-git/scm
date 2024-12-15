package com.hete.supply.scm.server.scm.production.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/24.
 */
@Data
public class AttrCategoryBo {
    @ApiModelProperty(value = "一级属性分类id（属性类型）", notes = "")
    private Long firstAttrCategoryId;

    @ApiModelProperty(value = "一级属性分类名称（属性类型名称）", notes = "")
    private String firstAttrCategoryName;

    @ApiModelProperty(value = "二级属性分类id（次级属性类型）", notes = "")
    private Long secondAttrCategoryId;

    @ApiModelProperty(value = "二级属性分类名称（次级属性类型名称）", notes = "")
    private String secondAttrCategoryName;
}
