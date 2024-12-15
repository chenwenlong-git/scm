package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/12 17:22
 */
@Data
@NoArgsConstructor
public class DevelopCancelDto {

    @NotBlank(message = "开发母单号不能为空")
    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

}
