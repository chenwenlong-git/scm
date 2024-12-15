package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.GradeType;
import com.hete.supply.scm.server.scm.enums.ProductionPlanReceivingStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 排产计划
 *
 * @author yanjiawei
 * @date 2023/07/31 08:01
 */
@Data
@NoArgsConstructor
@ApiModel(value = "排产计划", description = "排产计划")
public class ProcessProcedureEmployeePlanVo {
    @ApiModelProperty(value = "工序人员排产计划表主键id")
    private Long processProcedureEmployeePlanId;
    @ApiModelProperty(value = "加工单工序id")
    private Long processOrderProcedureId;
    @ApiModelProperty(value = "工序id")
    private Long processId;
    @ApiModelProperty(value = "工序名称")
    private String processSecondName;
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "职级类别")
    private GradeType gradeType;
    @ApiModelProperty(value = "职级名称")
    private String gradeName;
    @ApiModelProperty(value = "预计开始时间")
    private LocalDateTime expectBeginDateTime;
    @ApiModelProperty(value = "预计结束时间")
    private LocalDateTime expectEndDateTime;
    @ApiModelProperty(value = "排产计划接货状态枚举")
    private ProductionPlanReceivingStatus productionPlanReceivingStatus;
}