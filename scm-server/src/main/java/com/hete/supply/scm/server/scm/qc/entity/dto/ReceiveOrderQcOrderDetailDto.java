package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(description = "收货单质检订单明细列表")
public class ReceiveOrderQcOrderDetailDto {

    @ApiModelProperty(value = "批次码", required = true)
    private String batchCode;

    @ApiModelProperty(value = "容器编码", required = true)
    private String containerCode;

    @ApiModelProperty(value = "赫特SKU", required = true)
    private String skuCode;

    @ApiModelProperty(value = "待质检数量")
    private int receiveAmount;

    @JsonProperty(value = "platCode")
    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;
}
