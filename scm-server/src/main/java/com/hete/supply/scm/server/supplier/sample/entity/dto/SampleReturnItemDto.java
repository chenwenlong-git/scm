package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/18 14:41
 */
@Data
@NoArgsConstructor
public class SampleReturnItemDto {
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long sampleReturnOrderItemId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;

    @NotNull(message = "发货数量不能为空")
    @ApiModelProperty(value = "发货数量")
    private Integer returnCnt;

    @NotNull(message = "收货数量不能为空")
    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

}
