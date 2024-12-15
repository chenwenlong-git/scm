package com.hete.supply.scm.server.scm.production.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/20.
 */
@Data
public class UpdateSupSkuProdInfoDto {
    @NotBlank(message = "供应商编码不能为空")
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商原料属性列表")
    @JsonProperty("supplierMaterialAttrDetail")
    @Valid
    private List<UpdateSupSkuMaterialAttrDetailDto> supSkuMaterialAttrDetailDtoList;

    @ApiModelProperty(value = "供应商工艺属性列表")
    @JsonProperty("supplierCraftAttrDetail")
    @Valid
    private List<UpdateSupSkuCraftAttrDetailDto> supSkuCraftAttrDetailDtoList;

    @ApiModelProperty(value = "供应商商品属性列表")
    @JsonProperty("supplierSkuAttrInfoList")
    @Valid
    private List<SupSkuAttributeInfoDto> supSkuAttrInfoDtoList;

    @Size(max = 8, message = "封样图图片最多8张")
    @ApiModelProperty(value = "封样图列表")
    private List<String> samplePicList;
}
