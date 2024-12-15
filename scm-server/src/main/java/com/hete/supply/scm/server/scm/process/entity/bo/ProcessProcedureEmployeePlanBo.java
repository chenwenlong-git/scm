package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 工序人员分配计划
 *
 * @author yanjiawei
 * @date 2023/07/29 10:41
 */
@Data
@NoArgsConstructor
public class ProcessProcedureEmployeePlanBo extends ProcessPlanTimeBo {

    @ApiModelProperty(value = "工序人员排产计划表主键id")
    private Long processProcedureEmployeePlanId;
    @ApiModelProperty(value = "产能池编号")
    private String productionPoolCode;
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;
    @ApiModelProperty(value = "加工单工序表主键id")
    private Long processOrderProcedureId;
    @ApiModelProperty(value = "工序ID")
    private Long processId;
    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "工序产能数")
    private Integer processNum;
    @ApiModelProperty(value = "工序提成")
    private BigDecimal commission;
}