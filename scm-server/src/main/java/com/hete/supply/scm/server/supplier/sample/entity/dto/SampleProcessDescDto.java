package com.hete.supply.scm.server.supplier.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/3/31 09:30
 */
@Data
@NoArgsConstructor
public class SampleProcessDescDto {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderProcessDescId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值")
    private String descValue;
}
