package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/30 18:20
 */
@Data
@NoArgsConstructor
public class DevelopReviewOrderExportVo {
    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "审版单类型")
    private String developReviewOrderTypeStr;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "开发需求单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "审版人")
    private String reviewUsername;

    @ApiModelProperty(value = "审版时间")
    private String reviewDateStr;

    @ApiModelProperty(value = "审版结果")
    private String reviewResultStr;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品处理方式")
    private String developSampleMethodStr;

    @ApiModelProperty(value = "样品需求")
    private String developSampleDemandStr;

    @ApiModelProperty(value = "样品新旧程度")
    private String developSampleNewnessStr;

    @ApiModelProperty(value = "样品阶段")
    private String developSampleStageStr;

    @ApiModelProperty(value = "样品开发意见")
    private String developSampleDevOpinion;

    @ApiModelProperty(value = "样品质量")
    private String developSampleQualityStr;

    @ApiModelProperty(value = "样品需求来源")
    private String developReviewSampleSourceStr;

    @ApiModelProperty(value = "样品质量意见")
    private String developSampleQltyOpinion;

    @ApiModelProperty(value = "毛发异常类型：脱发、断发")
    private String abnormalHair;

    @ApiModelProperty(value = "网帽服帖度")
    private String meshCapFit;


    @ApiModelProperty(value = "毛发手感")
    private String hairFeel;

    @ApiModelProperty(value = "浮发情况")
    private String floatingHair;

    @ApiModelProperty(value = "生产属性")
    private String sampleInfoStr;


}
