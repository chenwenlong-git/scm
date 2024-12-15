package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: RockyHuas
 * @date: 2023/04/23 14:29
 */
@Data
@ApiModel(value = "limited加工单检查库存参数", description = "limited加工单检查库存参数")
public class ProcessOrderCheckMaterialDto {

    @ApiModelProperty(value = "加工单编号")
    @NotNull(message = "加工单编号不能为空")
    private String processOrderNo;


}
