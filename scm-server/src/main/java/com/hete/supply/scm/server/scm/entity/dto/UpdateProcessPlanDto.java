package com.hete.supply.scm.server.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:07
 */
@Data
@ApiModel(value = "修改排产参数", description = "修改排产参数")
public class UpdateProcessPlanDto {
    @ApiModelProperty(value = "排产计划")
    @JsonProperty("processProcedureEmployeePlans")
    @NotEmpty(message = "更新排产计划信息不能为空")
    private List<UpdateProcessProcedureEmployeePlanDto> processProcedureEmployeePlanDtoList;
}