package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.enums.SkuAvgPriceBizType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/31 14:12
 */
@Data
@NoArgsConstructor
public class SkuAndBatchCodeBo {
    @NotNull(message = "sku均价业务类型不能为空")
    @ApiModelProperty(value = "sku均价业务类型")
    private SkuAvgPriceBizType skuAvgPriceBizType;

    @NotEmpty(message = "sku与批次码列表不能为空")
    @Valid
    @ApiModelProperty(value = "sku与批次码列表")
    private List<SkuAndBatchCodeItemBo> skuAndBatchCodeItemBoList;
}
