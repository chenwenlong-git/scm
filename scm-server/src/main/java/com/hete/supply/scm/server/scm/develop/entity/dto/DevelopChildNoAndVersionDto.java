package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/8/6 10:32
 */
@Data
@NoArgsConstructor
public class DevelopChildNoAndVersionDto {
    @ApiModelProperty(value = "开发子单号")
    @NotNull(message = "开发子单号不能为空")
    private String developChildOrderNo;


    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
