package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;


/**
 * 工序关联的产能、排产计划信息
 *
 * @author yanjiawei
 * @date 2023/07/28 10:14
 */
@Data
@NoArgsConstructor
public class ProcessPlanRelateBo {
    @ApiModelProperty(value = "员工工序能力列表")
    private List<EmployeeProcessAbilityBo> employeeProcessAbilityBos = Collections.emptyList();
    @ApiModelProperty(value = "员工已排产列表")
    private List<ProcessProcedureEmployeePlanBo> processProcedureEmployeePlanBos = Collections.emptyList();
    @ApiModelProperty(value = "员工停工时间列表")
    private List<EmployeeRestTimeBo> employeeRestTimeBos = Collections.emptyList();
}