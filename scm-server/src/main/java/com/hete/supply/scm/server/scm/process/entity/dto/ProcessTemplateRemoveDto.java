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
@ApiModel(value = "工序模板删除参数", description = "工序模版删除参数")
public class ProcessTemplateRemoveDto {

    @ApiModelProperty(value = "工序模版 ID")
    @NotNull(message = "工序模版 ID 不能为空")
    private Long processTemplateId;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
