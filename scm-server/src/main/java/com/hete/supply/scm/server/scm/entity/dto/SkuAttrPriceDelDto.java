package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/12 11:43
 */
@Data
@NoArgsConstructor
public class SkuAttrPriceDelDto {
    @NotEmpty(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private List<Long> skuAttrPriceIdList;
}
