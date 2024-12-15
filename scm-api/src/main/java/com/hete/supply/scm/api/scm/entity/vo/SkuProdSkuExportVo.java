package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2024/10/21 18:19
 */
@Data
@NoArgsConstructor
public class SkuProdSkuExportVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private String skuDevType;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "是否在售")
    private String isSale;

    @ApiModelProperty(value = "在售平台")
    private String platName;

    @ApiModelProperty(value = "质量风险")
    private String skuRisk;

    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;

    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

    @ApiModelProperty(value = "克重")
    private BigDecimal weight;

    @ApiModelProperty(value = "公差")
    private BigDecimal tolerance;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "蕾丝面积")
    private String laceArea;

    @ApiModelProperty(value = "档长尺寸")
    private String fileLengthSize;

    @ApiModelProperty(value = "完成长尺寸")
    private String completeLongSize;

    @ApiModelProperty(value = "网帽大小")
    private String netCapSize;

    @ApiModelProperty(value = "刘海分式")
    private String partedBangs;

    @ApiModelProperty(value = "Parting分式")
    private String parting;

    @ApiModelProperty(value = "材料")
    private String material;

    @ApiModelProperty(value = "廓形")
    private String contour;

    @ApiModelProperty(value = "颜色色系")
    private String colorSystem;

    @ApiModelProperty(value = "颜色混合分区")
    private String colorMixPartition;

    @ApiModelProperty(value = "左侧档长尺寸")
    private String leftSideLength;

    @ApiModelProperty(value = "左侧完成长")
    private String leftFinish;

    @ApiModelProperty(value = "右侧档长尺寸")
    private String rightSideLength;

    @ApiModelProperty(value = "右侧完成长")
    private String rightFinish;

    @ApiModelProperty(value = "是否对称")
    private String symmetry;

    @ApiModelProperty(value = "预剪蕾丝")
    private String preselectionLace;
}
