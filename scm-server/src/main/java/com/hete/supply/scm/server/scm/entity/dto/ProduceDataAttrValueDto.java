package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/3/20 14:01
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrValueDto {

    @ApiModelProperty(value = "属性ID")
    @NotNull(message = "属性ID不能为空")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性value")
    @NotBlank(message = "属性value不能为空")
    private String attrValue;

}
