package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.enums.SampleResultStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/5/15 15:49
 */
@Data
@NoArgsConstructor
public class SampleChildResultExportVo {

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "选样结果id")
    private String sampleResultNo;

    @ApiModelProperty(value = "样品子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "选样结果")
    private SampleResult sampleResult;

    @ApiModelProperty(value = "选样结果名称")
    private String sampleResultName;

    @ApiModelProperty(value = "选样数量")
    private Integer sampleCnt;

    @ApiModelProperty(value = "处理状态")
    private SampleResultStatus sampleResultStatus;

    @ApiModelProperty(value = "处理状态名称")
    private String sampleResultStatusName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "关联单据")
    private String relateOrderNo;

    @ApiModelProperty(value = "单据数量")
    private Integer relateOrderAmount;

    @ApiModelProperty(value = "采购数量")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handlesTime;

    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

}
