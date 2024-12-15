package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 11:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PlmSkuSearchDto extends ComPageDto {

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "产品名称列表")
    private List<String> skuEncodeList;

    @ApiModelProperty("商品品类ID批量")
    @Size(max = 10, message = "商品类目搜索不能超过10个")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "质量风险批量")
    private List<SkuRisk> skuRiskList;

    @ApiModelProperty(value = "SKU批量")
    private List<String> skuList;

    @ApiModelProperty(value = "SPU批量")
    private List<String> spuList;

    @ApiModelProperty(value = "供应商产品名")
    private String supplierProductName;

    @ApiModelProperty(value = "供应商产品名批量")
    private List<String> supplierProductNameList;

    @ApiModelProperty(value = "SKU批量排除")
    private List<String> notSkuList;

    @ApiModelProperty("商品品类 ID")
    private Long categoryId;

    @ApiModelProperty(value = "是否绑定生产资料")
    private List<BindingProduceData> bindingProduceDataList;

    @ApiModelProperty(value = "plm的产品ID")
    private List<Long> plmSkuIdList;
}
