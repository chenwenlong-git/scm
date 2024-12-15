package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.plm.api.goods.enums.SkuDevType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopDetailBaseVo {
    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "商品类ID")
    private Long categoryId;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private SkuDevType skuDevType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "平台ID")
    private Long platformId;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "大货价格")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "开发子单属性列表")
    private List<DevelopChildOrderAttrVo> developChildOrderAttrList;


}
