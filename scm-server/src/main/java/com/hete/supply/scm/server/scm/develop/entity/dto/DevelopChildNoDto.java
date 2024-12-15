package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/8/4 09:33
 */
@Data
@NoArgsConstructor
public class DevelopChildNoDto {
    @NotBlank(message = "开发子单号不能为空")
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;
}
