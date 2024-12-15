package com.hete.supply.scm.server.scm.production.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class AttributeCategoryVo {

    @ApiModelProperty(value = "属性分类id")
    private Long attCategoryId;

    @ApiModelProperty(value = "属性名称")
    private String attrCategoryName;
}
