package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class DeductOrderVersionDto {

    @NotNull(message = "扣款单ID不能为空")
    @ApiModelProperty(value = "扣款单ID")
    private Long deductOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}