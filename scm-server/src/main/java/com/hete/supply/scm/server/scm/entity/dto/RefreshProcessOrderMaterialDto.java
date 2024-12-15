package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * 更新加工单原料信息
 *
 * @author yanjiawei
 * @date 2023/07/25 09:27
 */
@Data
@ApiModel(value = "更新加工单原料信息", description = "更新加工单原料信息")
public class RefreshProcessOrderMaterialDto {

    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;
}