package com.hete.supply.scm.server.scm.production.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 17:05
 */
@Data
public class SpecBookDetailVo {

    @ApiModelProperty(value = "供应商规格书详情列表")
    private List<SpecBookItemVo> specBookItemList;

    @ApiModelProperty(value = "商品属性列表")
    @JsonProperty("skuAttrInfoList")
    private List<SkuAttributeInfoVo> skuAttributeInfoVoList;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SpecBookItemVo extends SupSkuProdInfoVo {

        @ApiModelProperty(value = "供应商编码")
        private String supplierCode;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "spu")
        private String spu;

        @ApiModelProperty(value = "产品名称")
        private String skuEncode;

        @ApiModelProperty(value = "平台列表")
        @JsonProperty("platformList")
        private List<SpecPlatformVo> specPlatformVoList;

        @ApiModelProperty(value = "当前登录用户名称")
        private String loginUsername;

        @ApiModelProperty(value = "产品图片")
        private List<String> fileCodeList;
    }
}
