package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年08月11日 15:15
 */
@Data
@ApiModel(description = "H5排产工序信息")
public class H5ProcessInfoDto {
    @ApiModelProperty(value = "加工单编号")
    @NotBlank(message = "加工单编号不能为空")
    private String userKey;
}
