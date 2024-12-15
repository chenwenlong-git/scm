package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * @date 2023年07月03日 13:52
 */
@Data
public class MaterialBackDto {

    @NotBlank(message = "加工单号不能为空")
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;
}
