package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
@ApiModel("获取加工单生产资料")
public class ProcessOrderGenerateInfoDto {

    @NotBlank(message = "加工单成品SKU为空")
    @ApiModelProperty(value = "加工单成品SKU")
    private String processSku;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台", notes = "平台编码")
    private String platform;
}
