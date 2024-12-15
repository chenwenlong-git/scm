package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.server.scm.process.enums.ProcessStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "批量启用或禁用工序参数", description = "批量启用或禁用工序参数")
public class ProcessChangeStatusDto {

    @ApiModelProperty("工序")
    @NotEmpty(message = "工序不能为空")
    @Valid
    private List<Process> processes;

    @Data
    @ApiModel(value = "工序参数", description = "工序参数")
    public static class Process {

        @ApiModelProperty(value = "工序id")
        @NotNull(message = "工序id不能为空")
        private Long processId;

        @ApiModelProperty("版本号")
        @NotNull(message = "版本号不能为空")
        private Integer version;

    }

    @ApiModelProperty("工序状态,ENABLED:启用，DISABLED：禁用")
    @NotNull(message = "工序状态不能为空")
    private ProcessStatus processStatus;

}
