package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/8/8 11:31
 */
@Data
@NoArgsConstructor
public class DevelopReviewSampleInfoDto {
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "属性ID")
    @NotNull(message = "属性ID不能为空")
    private Long attributeNameId;

    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    @Length(max = 200, message = "属性值长度不能超过200位")
    private String sampleInfoValue;


    @ApiModelProperty(value = "评估意见")
    @Length(max = 32, message = "评估意见长度不能超过32位")
    private String evaluationOpinion;
}
