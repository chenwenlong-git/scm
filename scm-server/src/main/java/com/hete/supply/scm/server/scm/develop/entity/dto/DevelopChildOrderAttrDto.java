package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/8/6 10:44
 */
@Data
@NoArgsConstructor
public class DevelopChildOrderAttrDto {

    @NotNull(message = "属性ID不能为空")
    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @NotBlank(message = "属性key不能为空")
    @ApiModelProperty(value = "属性key")
    private String attrName;

    @NotBlank(message = "属性value不能为空")
    @ApiModelProperty(value = "属性value")
    @Length(max = 200, message = "属性值长度不能超过 200 位")
    private String attrValue;
}
