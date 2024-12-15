package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.plm.api.goods.enums.SkuDevType;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.supply.scm.server.scm.enums.BindingSupplierProduct;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 14:33
 */
@Data
@NoArgsConstructor
public class PlmSkuSearchVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "是否绑定供应商产品")
    private BindingSupplierProduct bindingSupplierProduct;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;

    @ApiModelProperty(value = "商品图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private SkuDevType skuDevType;

    @ApiModelProperty(value = "是否在售")
    private BooleanType isSale;

    @ApiModelProperty(value = "在售平台")
    private List<String> platNameList;

    @ApiModelProperty(value = "质量风险")
    private SkuRisk skuRisk;

    @ApiModelProperty(value = "单件产能")
    private BigDecimal singleCapacity;

    @ApiModelProperty(value = "生产周期")
    private BigDecimal cycle;

    @ApiModelProperty(value = "供应商的产品名称列表")
    private List<PlmSkuSearchSupplierItemVo> plmSkuSearchSupplierItemList;

}
