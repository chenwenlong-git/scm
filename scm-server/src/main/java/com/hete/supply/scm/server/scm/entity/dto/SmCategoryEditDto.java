package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2022/11/19 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SmCategoryEditDto extends SmCategoryIdAndVersionDto {
    @NotBlank(message = "类目名称不能为空")
    @ApiModelProperty(value = "类目名称")
    private String categoryName;
}
