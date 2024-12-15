package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/7/24.
 */
@Data
public class ProcRecOrderInfoDto {
    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;
}
