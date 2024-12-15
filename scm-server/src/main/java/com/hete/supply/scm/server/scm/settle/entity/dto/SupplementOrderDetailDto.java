package com.hete.supply.scm.server.scm.settle.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class SupplementOrderDetailDto {

    @NotBlank(message = "补款单号不能为空")
    @ApiModelProperty(value = "补款单号")
    private String supplementOrderNo;

}
