package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/2/21.
 */
@Data
public class SkuWeightedPriceQueryBo {
    @ApiModelProperty(value = "SKU", example = "ABC123")
    private String sku;

    @ApiModelProperty(value = "仓库编码", example = "WH001")
    private String warehouseCode;
}
