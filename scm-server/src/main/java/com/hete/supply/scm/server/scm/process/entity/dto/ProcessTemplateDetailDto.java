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
@ApiModel(value = "工序模板详情参数", description = "工序模版详情参数")
public class ProcessTemplateDetailDto {

    @ApiModelProperty(value = "工序模版 ID")
    @NotNull(message = "工序模版 ID 不能为空")
    private Long processTemplateId;
}
