package com.hete.supply.scm.server.scm.process.entity.dto;/**
 * 员工排产计划实体
 *
 * @author yanjiawei
 * Created on 2023/8/28.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * @date 2023年08月28日 23:53
 */
@ApiModel(description = "员工排产计划Dto")
@Data
public class EmployeeScheduleDto {
    @ApiModelProperty(value = "员工排产计划列表")
    @JsonProperty("processProcedureEmployeePlans")
    private List<GetProcessProcedureEmployeePlanDto> dtoList;
}
