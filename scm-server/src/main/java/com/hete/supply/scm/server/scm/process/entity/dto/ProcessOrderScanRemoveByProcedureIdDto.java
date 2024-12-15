package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2023/05/19 14:29
 */
@Data
@ApiModel(value = "通过加工工序删除加工扫码记录参数", description = "通过加工工序删除加工扫码记录参数")
public class ProcessOrderScanRemoveByProcedureIdDto {

    @ApiModelProperty(value = "加工工序 ID")
    @NotNull(message = "加工工序 ID 不能为空")
    @Valid
    private Long processOrderProcedureId;

}
