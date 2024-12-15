package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "加工单号查询参数", description = "加工单号查询参数")
@NoArgsConstructor
public class ProcessOrderNoDto {

    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;
}
