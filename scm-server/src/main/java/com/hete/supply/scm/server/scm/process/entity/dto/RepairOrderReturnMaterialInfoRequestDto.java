package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/2/4.
 */
@Data
@ApiModel(value = "返修单原料归还信息请求DTO")
public class RepairOrderReturnMaterialInfoRequestDto {
    @ApiModelProperty(value = "返修单号", example = "R123456")
    @NotBlank(message = "返修单号不能为空")
    private String repairOrderNo;
}
