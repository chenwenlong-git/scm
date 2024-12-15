package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/7 17:34
 */
@Data
@NoArgsConstructor
public class DevelopReviewSampleVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;

    @ApiModelProperty(value = "样品需求")
    private DevelopSampleDemand developSampleDemand;


    @ApiModelProperty(value = "样品质量")
    private DevelopSampleQuality developSampleQuality;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "样品新旧程度")
    private DevelopSampleNewness developSampleNewness;

    @ApiModelProperty(value = "样品阶段")
    private DevelopSampleStage developSampleStage;

    @ApiModelProperty(value = "需求来源")
    private DevelopReviewSampleSource developReviewSampleSource;

    @ApiModelProperty(value = "样品开发意见")
    private String developSampleDevOpinion;


    @ApiModelProperty(value = "样品质量意见")
    private String developSampleQltyOpinion;


    @ApiModelProperty(value = "毛发异常类型：脱发、断发")
    private String abnormalHair;


    @ApiModelProperty(value = "浮发情况")
    private String floatingHair;


    @ApiModelProperty(value = "网帽服帖度")
    private String meshCapFit;

    @ApiModelProperty(value = "毛发手感")
    private String hairFeel;

    @ApiModelProperty(value = "生产属性")
    private List<DevelopReviewSampleInfoVo> developReviewSampleInfoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "样品单类型")
    private DevelopSampleType developSampleType;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;
}
