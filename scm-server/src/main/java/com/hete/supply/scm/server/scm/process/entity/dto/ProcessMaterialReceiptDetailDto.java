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
@ApiModel(value = "加工原料收货详情参数", description = "加工原料收货详情参数")
public class ProcessMaterialReceiptDetailDto {

    @ApiModelProperty(value = "加工原料收货id")
    @NotNull(message = "加工原料收货id不能为空")
    private Long processMaterialReceiptId;
}
