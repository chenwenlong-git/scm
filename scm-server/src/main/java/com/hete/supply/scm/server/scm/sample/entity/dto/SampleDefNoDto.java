package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/9 23:04
 */
@Data
@NoArgsConstructor
public class SampleDefNoDto {
    @ApiModelProperty(value = "样品选样结果单号")
    private String sampleResultNo;
}
