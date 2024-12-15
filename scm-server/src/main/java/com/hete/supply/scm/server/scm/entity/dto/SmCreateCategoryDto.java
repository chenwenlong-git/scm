package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/19 14:57
 */
@Data
@NoArgsConstructor
public class SmCreateCategoryDto {
    @NotBlank(message = "品类名称不能为空")
    @ApiModelProperty(value = "品类名称")
    private String categoryName;

    @ApiModelProperty(value = "父级类目代码")
    private String parentCategoryCode;
}
