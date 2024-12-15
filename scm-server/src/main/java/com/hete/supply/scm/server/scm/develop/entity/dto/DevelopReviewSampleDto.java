package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/8 10:51
 */
@Data
@NoArgsConstructor
public class DevelopReviewSampleDto {
    @NotBlank(message = "样品单号不能为空")
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @NotNull(message = "克重不能为空")
    @ApiModelProperty(value = "克重")
    @DecimalMin(value = "0.01", message = "克重必须大于0")
    private BigDecimal gramWeight;

    @NotNull(message = "样品需求不能为空")
    @ApiModelProperty(value = "样品需求")
    private DevelopSampleDemand developSampleDemand;

    @NotNull(message = "样品质量不能为空")
    @ApiModelProperty(value = "样品质量")
    private DevelopSampleQuality developSampleQuality;

    @NotNull(message = "样品处理方式不能为空")
    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @NotNull(message = "样品新旧程度不能为空")
    @ApiModelProperty(value = "样品新旧程度")
    private DevelopSampleNewness developSampleNewness;

    @NotNull(message = "样品阶段不能为空")
    @ApiModelProperty(value = "样品阶段")
    private DevelopSampleStage developSampleStage;

    @ApiModelProperty(value = "样品开发意见")
    @Length(max = 500, message = "样品开发意见长度不能超过500位")
    private String developSampleDevOpinion;

    @ApiModelProperty(value = "样品质量意见")
    @Length(max = 500, message = "样品质量意见长度不能超过500位")
    private String developSampleQltyOpinion;

    @ApiModelProperty(value = "脱断发情况")
    @Length(max = 500, message = "脱断发情况不能超过500位")
    private String abnormalHair;

    @ApiModelProperty(value = "浮发情况")
    @Length(max = 500, message = "浮发情况长度不能超过500位")
    private String floatingHair;

    @ApiModelProperty(value = "网帽服帖度")
    @Length(max = 500, message = "网帽服帖度长度不能超过500位")
    private String meshCapFit;

    @ApiModelProperty(value = "毛发手感")
    @Length(max = 500, message = "毛发手感长度不能超过500位")
    private String hairFeel;

    @ApiModelProperty(value = "审版详情")
    @Valid
    private List<DevelopReviewSampleInfoDto> developReviewSampleInfoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @NotNull(message = "样品需求来源不能为空")
    @ApiModelProperty(value = "样品需求来源")
    private DevelopReviewSampleSource developReviewSampleSource;

    @ApiModelProperty(value = "渠道大货价格")
    private List<DevelopOrderPriceSaveDto> developOrderPriceList;
}
