package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.plm.api.developorder.enums.MaterialType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:10
 */
@Data
@NoArgsConstructor
public class ProduceDataItemRawListDto {

    @ApiModelProperty(value = "类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "原料SKU存在空SKU情况，请核对后再保存!")
    private String sku;

    @ApiModelProperty(value = "sku数量")
    @Min(value = 0, message = "sku数量必须大于等于0")
    private Integer skuCnt;


}
