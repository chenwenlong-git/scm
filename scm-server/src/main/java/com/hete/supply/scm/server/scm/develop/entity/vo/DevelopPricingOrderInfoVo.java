package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/2 19:21
 */
@Data
@NoArgsConstructor
public class DevelopPricingOrderInfoVo {

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "样品价格")
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

    @ApiModelProperty(value = "机制信息列表")
    private List<DevelopPricingOrderMechanismVo> developPricingOrderMechanismList;

    @ApiModelProperty(value = "开发审版关联样品单生产属性")
    private List<DevelopReviewSampleInfoVo> developReviewSampleInfoList;

    @ApiModelProperty(value = "渠道大货价格列表")
    private List<DevelopOrderPriceVo> developOrderPriceList;

}
