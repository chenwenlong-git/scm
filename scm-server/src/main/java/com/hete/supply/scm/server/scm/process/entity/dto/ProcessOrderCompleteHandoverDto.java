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
@ApiModel(value = "加工单完成交接参数", description = "加工单完成交接参数")
public class ProcessOrderCompleteHandoverDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "容器码")
    @NotBlank(message = "容器码不能为空")
    private String containerCode;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
