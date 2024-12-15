package com.hete.supply.scm.server.scm.sample.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/1/9 14:08
 */
@Data
@NoArgsConstructor
public class SampleDefectiveVo {
    @ApiModelProperty(value = "样品结果编号")
    private String sampleResultNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;
}
