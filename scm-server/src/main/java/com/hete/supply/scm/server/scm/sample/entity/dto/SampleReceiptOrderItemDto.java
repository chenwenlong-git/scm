package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/14 09:20
 */
@NoArgsConstructor
@Data
public class SampleReceiptOrderItemDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long sampleReceiptOrderItemId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @NotNull(message = "收货数不能为空")
    @ApiModelProperty(value = "收货数")
    private Integer receiptCnt;

    @NotNull(message = "发货数不能为空")
    @ApiModelProperty(value = "发货数")
    private Integer deliverCnt;
}
