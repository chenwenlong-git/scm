package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "通过加工单号查询扫码信息参数", description = "通过加工单号查询扫码信息参数")
public class ProcessOrderScanByNoDto {

    @ApiModelProperty(value = "加工单编号")
    @NotBlank(message = "加工单编号不能为空")
    private String processOrderNo;
}
