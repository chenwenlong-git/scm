package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:07
 */
@Data
@ApiModel(value = "手动排产参数", description = "手动排产参数")
public class DoProcessPlanDto {
    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private ProcessOrderType processOrderType;

    @ApiModelProperty(value = "排产计划")
    @JsonProperty("processProcedureEmployeePlans")
    @Valid
    @NotEmpty(message = "排产计划不能为空")
    private List<ProcessProcedureEmployeePlanDto> processProcedureEmployeePlanDtoList;
}