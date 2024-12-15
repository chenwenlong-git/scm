package com.hete.supply.scm.server.scm.develop.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/4 16:54
 */
@Data
@NoArgsConstructor
public class DevelopReviewSampleInfoVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "属性ID")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;


    @ApiModelProperty(value = "评估意见")
    private String evaluationOpinion;
}
