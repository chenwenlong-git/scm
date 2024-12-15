package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工扫码确认接货参数", description = "加工扫码确认接货参数")
public class ProcessOrderScanConfirmReceiveDto {

    @ApiModelProperty(value = "加工单编号")
    @NotBlank(message = "加工单编号不能为空")
    private String processOrderNo;

    @ApiModelProperty(value = "加工工序 ID")
    @NotNull(message = "加工工序ID不能为空")
    private Long processOrderProcedureId;

    @ApiModelProperty(value = "接货数量")
    @NotNull(message = "接货数量不能为空")
    @Positive(message = "接货数量必须是正整数")
    private Integer receiptNum;

}
