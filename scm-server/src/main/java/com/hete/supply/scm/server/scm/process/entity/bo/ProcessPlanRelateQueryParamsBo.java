package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;


/**
 * 查询加工单相关产能池信息
 *
 * @author yanjiawei
 * @date 2023/07/28 10:18
 */
@Data
@NoArgsConstructor
public class ProcessPlanRelateQueryParamsBo {
    @ApiModelProperty(value = "产能池编号")
    private String productionPoolCode;

    @ApiModelProperty(value = "工序id")
    private Set<Long> processIds;

    @ApiModelProperty(value = "员工编号")
    private Set<String> employeeNos;

    @ApiModelProperty(value = "排产时间")
    private TreeSet<LocalDate> processPlanDates;
}