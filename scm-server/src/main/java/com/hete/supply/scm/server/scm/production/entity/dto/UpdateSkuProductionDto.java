package com.hete.supply.scm.server.scm.production.entity.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.support.api.exception.ParamIllegalException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/9/18.
 */
@Data
public class UpdateSkuProductionDto {
    @NotBlank(message = "sku编码不能为空")
    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "克重")
    @DecimalMin(value = "0.00", inclusive = false, message = "克重必须大于0")
    @Digits(integer = 8, fraction = 2, message = "克重最多8位整数和2位小数")
    private BigDecimal weight;

    @ApiModelProperty(value = "克重版本号")
    private Integer weightVersion;

    @ApiModelProperty(value = "公差")
    @DecimalMin(value = "0.00", inclusive = true, message = "公差必须大于等于0")
    @Digits(integer = 8, fraction = 2, message = "公差最多8位整数和2位小数")
    private BigDecimal tolerance;

    @ApiModelProperty(value = "公差版本号")
    private Integer toleranceVersion;

    @ApiModelProperty(value = "商品属性值列表")
    @JsonProperty("skuAttrInfoList")
    private List<UpdateSkuAttributeDto> skuAttrInfoDtoList;

    @ApiModelProperty(value = "供应商商品生产信息列表")
    @JsonProperty("supplierSkuProdInfoList")
    @Valid
    private List<UpdateSupSkuProdInfoDto> updateSupSkuProdInfoDtoList;

    public void validate() {
        if (CollectionUtils.isNotEmpty(this.skuAttrInfoDtoList)) {
            //校验attrId不能重复
            List<Long> attrIds = this.skuAttrInfoDtoList.stream().map(UpdateSkuAttributeDto::getAttrId).distinct().collect(Collectors.toList());
            if (attrIds.size() != this.skuAttrInfoDtoList.size()) {
                throw new ParamIllegalException("更新商品属性失败！属性id重复，请校验后提交");
            }
        }

        if (CollectionUtils.isNotEmpty(this.updateSupSkuProdInfoDtoList)) {
            for (UpdateSupSkuProdInfoDto updateSupSkuProdInfoDto : updateSupSkuProdInfoDtoList) {
                List<SupSkuAttributeInfoDto> supSkuAttributeInfoDtoList = updateSupSkuProdInfoDto.getSupSkuAttrInfoDtoList();
                if (CollectionUtils.isNotEmpty(supSkuAttributeInfoDtoList)) {
                    List<Long> attrIds = supSkuAttributeInfoDtoList.stream().map(SupSkuAttributeInfoDto::getAttrId).distinct().collect(Collectors.toList());
                    if (attrIds.size() != supSkuAttributeInfoDtoList.size()) {
                        throw new ParamIllegalException("更新供应商商品属性失败！供应商商品属性id重复，请校验后提交");
                    }
                }
            }
        }
    }
}
