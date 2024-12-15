package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 10:37
 */
@Data
public class SampleInfoVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;

    @ApiModelProperty(value = "参照图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;
}
