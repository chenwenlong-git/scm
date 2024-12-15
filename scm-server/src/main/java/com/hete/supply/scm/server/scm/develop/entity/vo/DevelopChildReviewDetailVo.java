package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleDemand;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleQuality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 16:48
 */
@Data
@NoArgsConstructor
public class DevelopChildReviewDetailVo {
    @ApiModelProperty(value = "审版详情")
    private List<DevelopReviewSampleInfoVo> developReviewSampleInfoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;


    @ApiModelProperty(value = "样品需求")
    private DevelopSampleDemand developSampleDemand;


    @ApiModelProperty(value = "样品质量")
    private DevelopSampleQuality developSampleQuality;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;


    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;


}
