package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.enums.ProcessPlanStrategy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * 工序人员分配时间计划
 *
 * @author yanjiawei
 * @date 2023/07/28 18:02
 */
@Data
@Builder
public class ProcessEmployeePlanTimeParamBo {
    @ApiModelProperty(value = "员工编号")
    private String employeeNo;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "工序所需加工数")
    private Integer processNum;

    @ApiModelProperty(value = "工序总产能")
    private Integer processTotalCapacity;

    @ApiModelProperty(value = "期望开始时间")
    private LocalDateTime expectBeginTime;

    @ApiModelProperty(value = "上班开始时间")
    private LocalTime workStartTime;

    @ApiModelProperty(value = "上班结束时间")
    private LocalTime workEndTime;

    @ApiModelProperty(value = "休息时间")
    List<BreakTimeSegmentBo> breakTimeSegments;

    @ApiModelProperty(value = "排产日期")
    private TreeSet<LocalDate> processPlanDates;

    @ApiModelProperty(value = "节假日")
    private Set<LocalDate> holidays;

    @ApiModelProperty(value = "工作时长")
    private BigDecimal workHourDuration;

    @ApiModelProperty(value = "工序人员已存在排产计划信息")
    private List<ProcessProcedureEmployeePlanBo> existProductionSchedules;

    @ApiModelProperty(value = "工序人员休息时间")
    private List<EmployeeRestTimeBo> employeeRestTimes;

    @ApiModelProperty(value = "排产策略")
    private ProcessPlanStrategy processPlanStrategy;

}