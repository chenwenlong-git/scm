package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2024/1/8 11:24
 */
@Data
@NoArgsConstructor
public class RepairOrderNoDto {
    @NotBlank(message = "返修单号不能为空")
    @ApiModelProperty(value = "返修单号")
    private String repairOrderNo;
}
