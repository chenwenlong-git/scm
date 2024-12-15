package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单删除工序参数", description = "加工单删除工序参数")
public class ProcessOrderRemoveProcedureDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty("加工工序")
    @Valid
    private List<ProcessOrderRemoveProcedure> processOrderProcedures;

    @Data
    public static class ProcessOrderRemoveProcedure {

        @ApiModelProperty(value = "加工工序明细 id")
        private Long processOrderProcedureId;

        @ApiModelProperty("版本号")
        private Integer version;
    }

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
