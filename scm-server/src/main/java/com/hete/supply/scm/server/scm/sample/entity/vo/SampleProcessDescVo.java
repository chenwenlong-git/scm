package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/3/30 09:39
 */
@Data
@NoArgsConstructor
public class SampleProcessDescVo {
    @ApiModelProperty(value = "id")
    private Long sampleChildOrderProcessDescId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
