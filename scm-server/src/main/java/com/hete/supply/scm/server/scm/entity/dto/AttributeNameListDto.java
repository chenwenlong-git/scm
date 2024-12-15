package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/11 17:04
 */
@Data
@NoArgsConstructor
public class AttributeNameListDto {
    @NotEmpty(message = "属性名列表不能为空")
    @ApiModelProperty(value = "属性名列表")
    private List<String> attributeNameList;
}
