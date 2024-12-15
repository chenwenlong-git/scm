package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/3/14 10:49
 */
@Data
public class ProcessOrderDeliveryOrderDto {

    @ApiModelProperty(value = "加工单号")
    @NotBlank(message = "加工单号不能为空")
    private String processOrderNo;

}
