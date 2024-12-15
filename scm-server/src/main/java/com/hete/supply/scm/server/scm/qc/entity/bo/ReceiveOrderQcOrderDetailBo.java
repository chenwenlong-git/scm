package com.hete.supply.scm.server.scm.qc.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(description = "收货单质检明细")
public class ReceiveOrderQcOrderDetailBo {

    @ApiModelProperty(value = "批次码")
    @NotBlank(message = "批次码不能为空")
    private String batchCode;

    @ApiModelProperty(value = "容器编码", required = true)
    @NotBlank(message = "容器编码不能为空")
    private String containerCode;

    @ApiModelProperty(value = "赫特SKU")
    @NotBlank(message = "赫特SKU不能为空")
    private String skuCode;

    @ApiModelProperty(value = "待质检数量")
    private int waitAmount;

    @ApiModelProperty(value = "平台")
    private String platform;
}
