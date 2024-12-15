package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年08月08日 03:26
 */
@Data
@ApiModel(value = "H5加工详情查询参数", description = "H5加工详情查询参数")
public class ProcessScheduleInfoDto {
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;
}
