package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yanjiawei
 * Created on 2024/4/16.
 */
@Data
@ApiModel(description = "采购创建质检单DTO")
public class PurchaseQcCreateRequestDto {

    @NotBlank(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号", example = "PO123456", required = true)
    private String purchaseChildOrderNo;

}
