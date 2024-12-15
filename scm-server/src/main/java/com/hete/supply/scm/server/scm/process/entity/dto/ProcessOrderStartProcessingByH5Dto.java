package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工扫码开始工序参数", description = "加工扫码开始工序参数")
public class ProcessOrderStartProcessingByH5Dto {

    @ApiModelProperty(value = "加工单编号")
    @NotBlank(message = "加工单编号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "加工工序 ID")
    @NotNull(message = "加工工序ID不能为空")
    private Long processOrderProcedureId;

}
