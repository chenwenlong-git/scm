package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 工序人员分配时间计划
 *
 * @author yanjiawei
 * @date 2023/07/28 17:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePlanTimeBo extends ProcessPlanTimeBo {
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
}