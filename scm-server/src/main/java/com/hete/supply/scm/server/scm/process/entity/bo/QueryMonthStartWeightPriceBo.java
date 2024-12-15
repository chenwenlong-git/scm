package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/3/20.
 */
@Data
public class QueryMonthStartWeightPriceBo {
    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;
}
