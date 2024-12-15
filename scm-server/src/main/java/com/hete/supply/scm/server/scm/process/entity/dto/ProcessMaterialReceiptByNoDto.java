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
@ApiModel(value = "通过单号查询原料收货单参数", description = "通过单号查询原料收货单参数")
public class ProcessMaterialReceiptByNoDto {

    @ApiModelProperty(value = "加工单或者出库单号")
    @NotNull(message = "单号不能为空")
    private String no;
}
