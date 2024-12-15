package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单编辑工序参数", description = "加工单编辑工序参数")
public class ProcessOrderAddProcedureDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "加工数量")
    @NotNull(message = "加工数不能为空")
    @Positive(message = "加工数必须为正整数")
    private Integer processNum;

    @ApiModelProperty("加工工序")
    @NotEmpty
    @Valid
    private List<ProcessOrderAddProcedure> processOrderProcedures;

    @Data
    public static class ProcessOrderAddProcedure {

        @ApiModelProperty(value = "加工工序id")
        private Long processOrderProcedureId;

        @ApiModelProperty(value = "工序")
        @NotNull(message = "工序不能为空")
        private Long processId;

        @ApiModelProperty("版本号")
        private Integer version;
    }

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
