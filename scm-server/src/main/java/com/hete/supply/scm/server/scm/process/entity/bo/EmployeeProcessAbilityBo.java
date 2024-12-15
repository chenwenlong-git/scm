package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * @date 2023年07月29日 10:39
 */
@Data
@NoArgsConstructor
public class EmployeeProcessAbilityBo {

    @ApiModelProperty(value = "产能池主键id")
    private Long employeeProcessAbilityId;
    @ApiModelProperty(value = "工序id")
    private Long processId;
    @ApiModelProperty(value = "工序名称")
    private String processName;
    @ApiModelProperty(value = "产能池有效期截止时间")
    private LocalDateTime validityTime;
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "总加工数量（总产能）")
    private Integer totalProcessedNum;
    @ApiModelProperty(value = "可加工数（工序剩余产能）")
    private Integer availableProcessedNum;
    @ApiModelProperty(value = "已排产数")
    private Integer completePlanCount;
    @ApiModelProperty(value = "职级系数")
    private BigDecimal gradeLevel;
    @ApiModelProperty(value = "版本号")
    private Integer version;
}
