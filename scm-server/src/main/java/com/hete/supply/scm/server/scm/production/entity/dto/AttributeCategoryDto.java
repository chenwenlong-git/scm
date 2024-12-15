package com.hete.supply.scm.server.scm.production.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class AttributeCategoryDto {
    @ApiModelProperty(value = "父级属性分类id")
    private Long parentAttrCategoryId;
}
