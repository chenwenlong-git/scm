package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/7/23.
 */
@Data
public class MaterialInfoReqDto {
    @NotBlank(message = "加工单号不能为空")
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
}
