package com.hete.supply.scm.server.scm.production.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * sku生产属性信息
 *
 * @author yanjiawei
 * Created on 2024/9/18.
 */
@Data
public class SkuProdDetailVo {
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "克重")
    private BigDecimal weight;

    @ApiModelProperty(value = "克重版本号")
    private Integer weightVersion;

    @ApiModelProperty(value = "公差")
    private BigDecimal tolerance;

    @ApiModelProperty(value = "公差版本号")
    private Integer toleranceVersion;

    @ApiModelProperty(value = "商品属性列表")
    @JsonProperty("skuAttrInfoList")
    private List<SkuAttributeInfoVo> skuAttributeInfoVoList;

    @ApiModelProperty(value = "供应商商品生产信息列表(规格书共用)")
    @JsonProperty("supplierSkuProdInfoList")
    private List<SupSkuProdInfoVo> supSkuProdInfoVoList;
}
