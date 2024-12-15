package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "工序查询参数（通过sku或者模版名称）", description = "工序查询参数（通过sku或者模版名称）")
public class ProcessTemplateQueryByTemplateDto {

    @ApiModelProperty("产品 sku")
    private String sku;

    @ApiModelProperty("工序模版名称")
    private String processTemplateName;
}
