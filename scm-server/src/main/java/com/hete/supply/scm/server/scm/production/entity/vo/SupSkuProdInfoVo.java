package com.hete.supply.scm.server.scm.production.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 供应商商品生产信息
 *
 * @author yanjiawei
 * Created on 2024/9/20.
 */
@Data
public class SupSkuProdInfoVo {
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商原料属性列表")
    @JsonProperty("supplierMaterialAttrDetail")
    private List<SupSkuMaterialAttrDetailVo> supSkuMaterialAttrDetailVoList;

    @ApiModelProperty(value = "供应商工艺属性列表")
    @JsonProperty("supplierCraftAttrDetail")
    private List<SupSkuCraftAttrDetailVo> supSkuCraftAttrDetailVoList;

    @ApiModelProperty(value = "供应商商品属性列表")
    @JsonProperty("supplierSkuAttrInfoList")
    private List<SupSkuAttributeInfoVo> supSkuAttributeInfoVoList;

    @ApiModelProperty(value = "封样图列表")
    private List<String> samplePicList;

    @ApiModelProperty(value = "是否维护")
    private BooleanType isMaintain;

    @ApiModelProperty(value = "平台列表,方便前端映射需要")
    @JsonProperty("platformList")
    private List<SpecPlatformVo> specPlatformVoList;
}
