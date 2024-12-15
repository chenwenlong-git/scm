package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.server.scm.process.enums.ProcessOrderIsForward;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单状态变更参数", description = "加工单状态变更参数")
public class ProcessOrderChangeStatusDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单流转，TRUE : 正常流程，FALSE：回退")
    @NotNull(message = "加工单流转标识不能为空")
    private ProcessOrderIsForward isForward;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
