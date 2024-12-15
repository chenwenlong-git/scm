package com.hete.supply.scm.server.scm.develop.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderInfoDto {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品价格")
    @NotNull(message = "样品价格不能为空")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "5*5普通")
    private String ordinary;

    @ApiModelProperty(value = "前头发块尺寸")
    private String frontSize;

    @ApiModelProperty(value = "手织毛尺寸")
    private String handWeavingSize;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "手织克重")
    private BigDecimal gramWeight;

    @ApiModelProperty(value = "手织毛金额")
    private BigDecimal handWeavingPrice;

    @ApiModelProperty(value = "机制信息列表")
    private List<DevelopPricingOrderMechanismDto> developPricingOrderMechanismList;

    @ApiModelProperty(value = "手勾费")
    private BigDecimal handHookPrice;

    @ApiModelProperty(value = "生产费用")
    private BigDecimal productionPrice;

    @ApiModelProperty(value = "网帽")
    private BigDecimal meshCap;

    @ApiModelProperty(value = "曲度费")
    private BigDecimal curvaturePrice;

    @ApiModelProperty(value = "染色费")
    private BigDecimal stainPrice;

    @ApiModelProperty(value = "合计成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "工厂利润")
    private BigDecimal factoryProfit;

    @ApiModelProperty(value = "报价")
    private BigDecimal quotedPrice;

    @ApiModelProperty(value = "半成品加权总价")
    private BigDecimal weightedPrice;

    @ApiModelProperty(value = "加工工序费")
    private BigDecimal secondPrice;

    @ApiModelProperty(value = "管理费")
    private BigDecimal managePrice;

    @ApiModelProperty(value = "总成本价")
    private BigDecimal costTotalPrice;

    @ApiModelProperty(value = "渠道大货价格")
    @NotEmpty(message = "渠道大货价格不能为空")
    private List<@Valid DevelopOrderPriceSaveDto> developOrderPriceList;

}
