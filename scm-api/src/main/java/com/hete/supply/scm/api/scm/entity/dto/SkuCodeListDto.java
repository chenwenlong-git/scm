package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/28 10:53
 */
@Data
@NoArgsConstructor
public class SkuCodeListDto {
    @ApiModelProperty("sku")
    @NotEmpty(message = "sku列表不能为空")
    private List<String> skuCodeList;
}
