package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/26 14:05
 */
@Data
@NoArgsConstructor
public class DevelopReviewUnusualVo {
    @ApiModelProperty(value = "异常报告单号")
    private String developReviewOrderUnusualNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "审版人")
    private String reviewUser;


    @ApiModelProperty(value = "审版人")
    private String reviewUsername;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "审版数量")
    private Integer developSampleNum;

    @ApiModelProperty(value = "开发人")
    private String devUser;


    @ApiModelProperty(value = "开发人")
    private String devUsername;

    @ApiModelProperty(value = "审版时间")
    private LocalDateTime reviewDate;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "不良数量")
    private Integer poorAmount;

    @ApiModelProperty(value = "系统需求信息")
    private List<DevelopChildOrderAttrVo> developChildOrderAttrVoList;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "审版记录")
    private List<DevelopReviewSampleInfoVo> developReviewSampleInfoList;

    @ApiModelProperty(value = "样品新旧程度(产品阶段)")
    private DevelopSampleNewness developSampleNewness;

    @ApiModelProperty(value = "样品阶段")
    private DevelopSampleStage developSampleStage;

    @ApiModelProperty(value = "样品需求来源")
    private DevelopReviewSampleSource developReviewSampleSource;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "样品质量(质量结论)")
    private DevelopSampleQuality developSampleQuality;

    @ApiModelProperty(value = "样品需求(需求结论)")
    private DevelopSampleDemand developSampleDemand;

    @ApiModelProperty(value = "异常现象")
    private String phenomena;

    @ApiModelProperty(value = "异常图片")
    private List<String> fileCodeList;


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
