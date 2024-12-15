package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/19 16:25
 */
@Data
@NoArgsConstructor
public class SmCategoryIdAndVersionDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long smCategoryId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
