package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderDetailDto {

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "规格书文件")
    private List<String> specificationsFileCodeList;

    @ApiModelProperty(value = "报价单文件")
    private List<String> quotationFileCodeList;

    @ApiModelProperty(value = "工序")
    private List<DevelopSampleOrderProcessDto> developSampleOrderProcessList;

    @ApiModelProperty(value = "工序描述")
    private List<DevelopSampleOrderProcessDescDto> developSampleOrderProcessDescList;

    @ApiModelProperty(value = "原料")
    private List<DevelopSampleOrderRawDto> developSampleOrderRawList;

    @Length(max = 200, message = "工序补充不能超过200个字符")
    @ApiModelProperty(value = "工序补充")
    private String processRemarks;

    @DecimalMax(value = "99999999.99", message = "供应商样品报价不能超过1亿")
    @ApiModelProperty(value = "供应商样品报价")
    private BigDecimal supplierSamplePrice;

    @ApiModelProperty(value = "样品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "供应商渠道大货价格")
    private List<DevelopOrderPriceSaveDto> developOrderPriceList;

}
