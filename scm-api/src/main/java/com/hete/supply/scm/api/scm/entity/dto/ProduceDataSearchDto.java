package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/3/28 15:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProduceDataSearchDto extends PlmSkuSearchDto {

    @ApiModelProperty("商品品类 ID")
    private Long categoryId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "SPU")
    private String spu;

    @ApiModelProperty(value = "SKU批量")
    private List<String> skuList;

    @ApiModelProperty(value = "SKU批量排除")
    private List<String> notSkuList;

    @ApiModelProperty(value = "状态")
    private List<BindingProduceData> bindingProduceDataList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "plm的产品ID")
    private List<Long> plmSkuIdList;

    @ApiModelProperty(value = "SPU批量(勾选)")
    private List<String> spuList;


}
