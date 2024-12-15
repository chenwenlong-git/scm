package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年07月31日 08:10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "更新排产计划", description = "更新排产计划")
public class UpdateProcessProcedureEmployeePlanDto {


    @ApiModelProperty(value = "工序人员排产计划表主键id")
    @NotNull(message = "工序人员排产计划表主键id不能为空")
    private Long processProcedureEmployeePlanId;

    @NotNull(message = "加工单工序id不能为空")
    @ApiModelProperty(value = "加工单工序id")
    private Long processOrderProcedureId;

    @NotNull(message = "工序id不能为空")
    @ApiModelProperty(value = "工序id")
    private Long processId;

    @NotBlank(message = "工序名称不能为空")
    @ApiModelProperty(value = "工序名称")
    private String processSecondName;

    @NotBlank(message = "员工编号不能为空")
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @NotBlank(message = "员工名称不能为空")
    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @NotNull(message = "预计结束时间不能为空")
    @ApiModelProperty(value = "预计开始时间")
    private LocalDateTime expectBeginDateTime;

    @NotNull(message = "预计结束时间不能为空")
    @ApiModelProperty(value = "预计结束时间")
    private LocalDateTime expectEndDateTime;
}
