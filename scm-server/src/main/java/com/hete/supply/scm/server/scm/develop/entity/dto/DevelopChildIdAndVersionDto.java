package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/29 10:55
 */
@Data
@NoArgsConstructor
public class DevelopChildIdAndVersionDto {
    @ApiModelProperty(value = "开发子单ID")
    @NotNull(message = "开发子单ID不能为空")
    private Long developChildOrderId;

    @ApiModelProperty(value = "version")
    @NotNull(message = "version不能为空")
    private Integer version;
}
