package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/14 14:28
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderDetailVo {

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "状态")
    private DevelopSampleStatus developSampleStatus;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "规格书文件")
    private List<String> specificationsFileCodeList;

    @ApiModelProperty(value = "报价单文件")
    private List<String> quotationFileCodeList;

    @ApiModelProperty(value = "工序")
    private List<DevelopSampleOrderProcessVo> developSampleOrderProcessList;

    @ApiModelProperty(value = "工序描述")
    private List<DevelopSampleOrderProcessDescVo> developSampleOrderProcessDescList;

    @ApiModelProperty(value = "原料")
    private List<DevelopSampleOrderRawVo> developSampleOrderRawList;

    @ApiModelProperty(value = "工序补充")
    private String processRemarks;

    @ApiModelProperty(value = "供应商样品报价")
    private BigDecimal supplierSamplePrice;


    @ApiModelProperty(value = "样品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "审版信息")
    private DevelopChildReviewDetailVo developChildReviewDetail;

    @ApiModelProperty(value = "核价信息")
    private DevelopChildReviewInfoVo developChildReviewInfo;

    @ApiModelProperty(value = "供应商大货报价列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;


}
