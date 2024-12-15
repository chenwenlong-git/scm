package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/30 16:59
 */
@Data
@NoArgsConstructor
public class ProcessEmployeePlanTimeBo extends ProcessPlanTimeBo {
    @ApiModelProperty(value = "工序id")
    private Long processId;
    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
}