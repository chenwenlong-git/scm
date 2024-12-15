package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/10 11:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleChildOrderDto extends SampleSplitDto.SampleSplitItem {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long sampleChildOrderId;

    @NotNull(message = "version不能为空")
    @ApiModelProperty(value = "version")
    private Integer version;
}
