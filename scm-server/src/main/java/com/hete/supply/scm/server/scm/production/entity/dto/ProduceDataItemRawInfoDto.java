package com.hete.supply.scm.server.scm.production.entity.dto;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.common.util.ParamValidUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/9/27 15:25
 */
@Data
@NoArgsConstructor
public class ProduceDataItemRawInfoDto {

    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "原料SKU存在空SKU情况，请核对后再保存!")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    @Positive(message = "sku数量必须大于0")
    private Integer skuCnt;

    @ApiModelProperty(value = "商品对照关系")
    @JsonProperty("prodRawCompareList")
    private List<@Valid ProduceDataItemRawCompareDto> prodRawCompareDtoList;

    //校验
    public void validateSkuCompareDtoList() {
        // 校验sku是否重复
        if (CollectionUtils.isEmpty(this.prodRawCompareDtoList)) {
            return;
        }

        // 获取去重后的 SKU 列表
        List<String> skuList = this.prodRawCompareDtoList.stream()
                .map(ProduceDataItemRawCompareDto::getSku)
                .distinct()
                .collect(Collectors.toList());

        // 验证去重后的 SKU 列表大小是否与原列表相同，若不同则抛出异常
        ParamValidUtils.requireEquals(skuList.size(), this.prodRawCompareDtoList.size(), "商品对照关系列表中SKU重复，请校验后提交");
    }
}
