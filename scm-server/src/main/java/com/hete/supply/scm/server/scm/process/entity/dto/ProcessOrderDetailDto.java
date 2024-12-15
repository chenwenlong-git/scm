package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单详情参数", description = "加工单详情参数")
public class ProcessOrderDetailDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;
}
