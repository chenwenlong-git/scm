package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 排产工序人员
 *
 * @author yanjiawei
 * @date 2023/07/28 14:11
 */
@Data
@NoArgsConstructor
public class ProcessPlanEmployeeBo extends ProcessPlanTimeBo {

    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "工序id")
    private Long processId;
    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "工序总产能")
    private Integer processTotalCapacity;
    @ApiModelProperty(value = "可加工数（工序剩余产能）")
    private Integer availableProcessedNum;
    @ApiModelProperty(value = "已排产数")
    private Integer completePlanCount;
    @ApiModelProperty(value = "职级系数")
    private BigDecimal gradeLevel;


}