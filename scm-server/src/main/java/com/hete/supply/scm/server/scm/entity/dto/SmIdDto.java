package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/19 17:41
 */
@Data
@NoArgsConstructor
public class SmIdDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long subsidiaryMaterialId;
}
