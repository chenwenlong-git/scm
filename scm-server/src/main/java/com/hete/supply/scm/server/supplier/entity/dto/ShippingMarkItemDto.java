package com.hete.supply.scm.server.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/2/14 00:56
 */
@Data
@NoArgsConstructor
public class ShippingMarkItemDto {
    @NotBlank(message = "发货单号不能为空")
    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @NotBlank(message = "业务子单单号不能为空")
    @ApiModelProperty(value = "业务子单单号")
    private String bizChildOrderNo;

    @NotBlank(message = "箱唛箱号（序号）不能为空")
    @ApiModelProperty(value = "箱唛箱号（序号）")
    private String shippingMarkNum;

    @NotNull(message = "发货数不能为空")
    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;
}
