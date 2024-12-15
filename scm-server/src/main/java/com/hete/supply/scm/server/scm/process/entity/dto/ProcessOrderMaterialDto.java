package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.enums.MaterialSkuType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/11 11:15
 */
@Data
@NoArgsConstructor
public class ProcessOrderMaterialDto {
    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku 不能为空")
    @Length(max = 100, message = "sku字符长度不能超过 100 位")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    @Length(max = 100, message = "sku批次码字符长度不能超过 100 位")
    private String skuBatchCode;

    @ApiModelProperty(value = "出库数量（单个加工需求数）")
    @NotNull(message = "出库数量不能为空")
    @Positive(message = "出库数量必须为正整数")
    private Integer deliveryNum;

    @ApiModelProperty(value = "原料SKU所属类型")
    private MaterialSkuType materialSkuType;

    @ApiModelProperty(value = "商品对照关系")
    @JsonProperty("processOrderMaterialCompareList")
    private List<@Valid ProcessOrderMaterialCompareDto> processOrderMaterialCompareDtoList;
}
