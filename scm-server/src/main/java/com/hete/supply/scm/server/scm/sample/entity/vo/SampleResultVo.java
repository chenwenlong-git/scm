package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/22 15:02
 */
@Data
@NoArgsConstructor
public class SampleResultVo {
    @ApiModelProperty(value = "选样人")
    private String sampleUsername;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "入库单号")
    private String sampleWarehousingOrderNo;

    @ApiModelProperty(value = "选样结果")
    private SampleResult sampleResult;

    @ApiModelProperty(value = "选样数量")
    private Integer sampleCnt;


    @ApiModelProperty(value = "样品结果编号")
    private String sampleResultNo;

    @ApiModelProperty(value = "选样结果说明")
    private String remark;
}
