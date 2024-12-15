package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/2 23:41
 */
@Data
@NoArgsConstructor
public class SampleReceiptIdAndVersionDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long sampleReceiptOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
