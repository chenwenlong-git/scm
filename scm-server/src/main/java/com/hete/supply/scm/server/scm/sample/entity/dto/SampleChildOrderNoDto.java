package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/9 10:15
 */
@Data
@NoArgsConstructor
public class SampleChildOrderNoDto {
    @NotNull(message = "样品采购子单号为必填值")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
