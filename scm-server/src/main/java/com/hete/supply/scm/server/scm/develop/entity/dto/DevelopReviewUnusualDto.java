package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/26 10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopReviewUnusualDto extends DevelopReviewNoDto {
    @NotBlank(message = "样品单号不能为空")
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @Length(max = 255, message = "异常现象内容不能超过255个字")
    @ApiModelProperty(value = "异常现象")
    private String phenomena;

    @ApiModelProperty(value = "异常图片")
    private List<String> fileCodeList;

    @Length(max = 255, message = "质量分析内容不能超过255个字")
    @ApiModelProperty(value = "质量分析")
    private String qualityAnalysis;


    @ApiModelProperty(value = "质量分析负责人")
    private String qualityAnalysisUser;


    @ApiModelProperty(value = "质量分析负责人")
    private String qualityAnalysisUsername;


    @ApiModelProperty(value = "质量分析时间")
    private LocalDateTime qualityAnalysisDate;

    @Length(max = 255, message = "需求分析内容不能超过255个字")
    @ApiModelProperty(value = "需求分析")
    private String demandAnalysis;


    @ApiModelProperty(value = "需求分析负责人")
    private String demandAnalysisUser;


    @ApiModelProperty(value = "需求分析负责人")
    private String demandAnalysisUsername;


    @ApiModelProperty(value = "需求分析时间")
    private LocalDateTime demandAnalysisDate;


    @Length(max = 255, message = "改善对策内容不能超过255个字")
    @ApiModelProperty(value = "改善对策")
    private String improve;


    @ApiModelProperty(value = "改善对策负责人")
    private String improveUser;


    @ApiModelProperty(value = "改善对策负责人")
    private String improveUsername;


    @ApiModelProperty(value = "改善对策时间")
    private LocalDateTime improveDate;

    @Length(max = 255, message = "效果验证内容不能超过255个字")
    @ApiModelProperty(value = "效果验证")
    private String validation;


    @ApiModelProperty(value = "效果验证负责人")
    private String validationUser;


    @ApiModelProperty(value = "效果验证负责人")
    private String validationUsername;


    @ApiModelProperty(value = "效果验证时间")
    private LocalDateTime validationDate;
}
