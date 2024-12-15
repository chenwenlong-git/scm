package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2024/1/10 15:48
 */
@Data
public class ProcessOrderPrintBo {

    @NotBlank(message = "加工单号不能为空")
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "出库单号")
    private String deliveryNo;
}
