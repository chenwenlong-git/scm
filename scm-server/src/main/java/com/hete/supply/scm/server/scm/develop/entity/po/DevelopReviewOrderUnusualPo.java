package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 开发审版异常处理报告表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_review_order_unusual")
@ApiModel(value = "DevelopReviewOrderUnusualPo对象", description = "开发审版异常处理报告表")
public class DevelopReviewOrderUnusualPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_review_order_unusual_id", type = IdType.ASSIGN_ID)
    private Long developReviewOrderUnusualId;

    @ApiModelProperty(value = "异常报告单号")
    private String developReviewOrderUnusualNo;

    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "异常现象")
    private String phenomena;


    @ApiModelProperty(value = "质量分析")
    private String qualityAnalysis;


    @ApiModelProperty(value = "质量分析负责人")
    private String qualityAnalysisUser;


    @ApiModelProperty(value = "质量分析负责人")
    private String qualityAnalysisUsername;


    @ApiModelProperty(value = "质量分析时间")
    private LocalDateTime qualityAnalysisDate;


    @ApiModelProperty(value = "需求分析")
    private String demandAnalysis;


    @ApiModelProperty(value = "需求分析负责人")
    private String demandAnalysisUser;


    @ApiModelProperty(value = "需求分析负责人")
    private String demandAnalysisUsername;


    @ApiModelProperty(value = "需求分析时间")
    private LocalDateTime demandAnalysisDate;


    @ApiModelProperty(value = "改善对策")
    private String improve;


    @ApiModelProperty(value = "改善对策负责人")
    private String improveUser;


    @ApiModelProperty(value = "改善对策负责人")
    private String improveUsername;


    @ApiModelProperty(value = "改善对策时间")
    private LocalDateTime improveDate;


    @ApiModelProperty(value = "效果验证")
    private String validation;


    @ApiModelProperty(value = "效果验证负责人")
    private String validationUser;


    @ApiModelProperty(value = "效果验证负责人")
    private String validationUsername;


    @ApiModelProperty(value = "效果验证时间")
    private LocalDateTime validationDate;

}
