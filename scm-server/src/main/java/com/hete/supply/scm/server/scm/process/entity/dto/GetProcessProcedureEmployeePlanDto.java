package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年08月03日 16:08
 */
@Data
@ApiModel(value = "获取员工排产池计划", description = "获取员工排产池计划")
public class GetProcessProcedureEmployeePlanDto {

    @NotBlank(message = "员工编号不能为空")
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @NotNull(message = "排产开始时间不能为空")
    @ApiModelProperty(value = "排产开始时间")
    private LocalDateTime expectBeginTime;

    @NotNull(message = "排产结束时间不能为空")
    @ApiModelProperty(value = "排产结束时间")
    private LocalDateTime expectEndTime;
}
