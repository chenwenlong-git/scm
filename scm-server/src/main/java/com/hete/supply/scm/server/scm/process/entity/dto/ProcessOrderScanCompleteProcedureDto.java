package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工扫码完成工序参数", description = "加工扫码完成工序参数")
public class ProcessOrderScanCompleteProcedureDto {

    @ApiModelProperty(value = "加工单编号")
    @NotBlank(message = "加工单编号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "加工工序 ID")
    @NotNull(message = "加工工序ID不能为空")
    private Long processOrderProcedureId;

    @ApiModelProperty(value = "正品数量")
    @NotNull(message = "正品数不能为空")
    @PositiveOrZero(message = "正品数不能小于0")
    private Integer qualityGoodsCnt;

    @ApiModelProperty(value = "次品数量")
    @NotNull(message = "次品数不能为空")
    @PositiveOrZero(message = "次品数不能小于0")
    private Integer defectiveGoodsCnt;

}
