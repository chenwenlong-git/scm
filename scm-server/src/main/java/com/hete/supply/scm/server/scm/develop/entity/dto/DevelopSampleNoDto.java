package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author weiwenxin
 * @date 2023/8/7 18:03
 */
@Data
@NoArgsConstructor
public class DevelopSampleNoDto {
    @NotBlank(message = "样品单号不能为空")
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;
}
