package com.hete.supply.scm.api.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/18 15:50
 */
@Data
@NoArgsConstructor
public class SkuBatchCodeDto {
    @ApiModelProperty(value = "sku批次码")
    @NotEmpty(message = "sku批次码不能为空")
    @Size(max = 200, message = "sku批次码数量不能超过200个")
    private List<String> skuBatchCodeList;
}
