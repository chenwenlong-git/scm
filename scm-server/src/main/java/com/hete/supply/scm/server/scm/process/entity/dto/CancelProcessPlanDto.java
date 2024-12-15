package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 工序人员时间
 *
 * @author yanjiawei
 * @date 2023/07/31 07:07
 */
@Data
@ApiModel(value = "取消排产参数", description = "取消排产参数")
public class CancelProcessPlanDto {
    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;
}